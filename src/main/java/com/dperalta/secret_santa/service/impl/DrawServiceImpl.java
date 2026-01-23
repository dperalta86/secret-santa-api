package com.dperalta.secret_santa.service.impl;

import com.dperalta.secret_santa.dto.request.CreateDrawRequest;
import com.dperalta.secret_santa.dto.request.ParticipantRequest;
import com.dperalta.secret_santa.dto.response.AssignmentResponse;
import com.dperalta.secret_santa.dto.response.DrawDetailResponse;
import com.dperalta.secret_santa.dto.response.DrawResponse;
import com.dperalta.secret_santa.exception.DrawAlreadyExecutedException;
import com.dperalta.secret_santa.exception.DrawNotFoundException;
import com.dperalta.secret_santa.exception.DuplicateParticipantException;
import com.dperalta.secret_santa.exception.InsufficientParticipantsException;
import com.dperalta.secret_santa.model.Draw;
import com.dperalta.secret_santa.model.DrawStatus;
import com.dperalta.secret_santa.model.Participant;
import com.dperalta.secret_santa.repository.DrawRepository;
import com.dperalta.secret_santa.repository.ParticipantRepository;
import com.dperalta.secret_santa.service.DrawService;
import com.dperalta.secret_santa.service.NotificationService;
import com.dperalta.secret_santa.utils.CodeGenerator;
import com.dperalta.secret_santa.utils.DrawAlgorithm;
import com.dperalta.secret_santa.utils.DrawMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final DrawRepository drawRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public DrawResponse createDraw(CreateDrawRequest request) {
        log.info("Creating new draw: {}", request.getName());

        // Validar número mínimo de participantes
        if (request.getParticipants().size() < 3) {
            throw new InsufficientParticipantsException();
        }

        // Generar código único
        String code = CodeGenerator.generateUnique(drawRepository::existsByCode);
        log.debug("Generated unique code: {}", code);

        // Crear entidad Draw
        Draw draw = Draw.builder()
                .code(code)
                .name(request.getName())
                .description(request.getDescription())
                .drawDate(request.getDrawDate())
                .budgetLimit(request.getBudgetLimit())
                .status(DrawStatus.PENDING)
                .participants(new ArrayList<>())
                .build();

        // Crear y agregar participantes
        for (ParticipantRequest participantReq : request.getParticipants()) {
            // Validar email duplicado
            if (draw.getParticipants().stream()
                    .anyMatch(p -> p.getEmail().equalsIgnoreCase(participantReq.getEmail()))) {
                throw new DuplicateParticipantException(participantReq.getEmail());
            }

            Participant participant = Participant.builder()
                    .name(participantReq.getName())
                    .email(participantReq.getEmail())
                    .phone(participantReq.getPhone())
                    .notificationSent(false)
                    .build();

            draw.addParticipant(participant);
        }

        // Guardar
        Draw savedDraw = drawRepository.save(draw);
        log.info("Draw created successfully with code: {}", savedDraw.getCode());

        return DrawMapper.toDrawResponse(savedDraw);
    }

    @Override
    @Transactional(readOnly = true)
    public DrawResponse getDrawByCode(String code) {
        log.debug("Fetching draw by code: {}", code);

        Draw draw = drawRepository.findByCode(code)
                .orElseThrow(() -> new DrawNotFoundException(code));

        return DrawMapper.toDrawResponse(draw);
    }

    @Override
    @Transactional(readOnly = true)
    public DrawDetailResponse getDrawDetailByCode(String code) {
        log.debug("Fetching draw details by code: {}", code);

        Draw draw = drawRepository.findByCodeWithParticipants(code)
                .orElseThrow(() -> new DrawNotFoundException(code));

        return DrawMapper.toDrawDetailResponse(draw);
    }

    @Override
    @Transactional
    public DrawResponse executeDraw(String code) {
        log.info("Executing draw for code: {}", code);

        // Buscar draw
        Draw draw = drawRepository.findByCodeWithParticipants(code)
                .orElseThrow(() -> new DrawNotFoundException(code));

        // Validar que no haya sido ejecutado
        if (draw.getStatus() == DrawStatus.DRAWN || draw.getStatus() == DrawStatus.COMPLETED) {
            throw new DrawAlreadyExecutedException(code);
        }

        // Validar participantes suficientes
        if (draw.getParticipants().size() < 3) {
            throw new InsufficientParticipantsException();
        }

        // Realizar sorteo
        Map<Participant, Participant> assignments = DrawAlgorithm.performDraw(draw.getParticipants());
        log.debug("Draw algorithm completed. Assigning participants...");

        // Asignar resultados
        for (Map.Entry<Participant, Participant> entry : assignments.entrySet()) {
            Participant giver = entry.getKey();
            Participant receiver = entry.getValue();

            giver.setAssignedTo(receiver);
            log.debug("Assigned: {} -> {}", giver.getEmail(), receiver.getEmail());
        }

        // Actualizar estado
        draw.setStatus(DrawStatus.DRAWN);
        Draw savedDraw = drawRepository.save(draw);

        // Enviar notificaciones (asíncrono idealmente, pero por ahora síncrono)
        try {
            notificationService.sendAllAssignments(savedDraw);
        } catch (Exception e) {
            log.error("Error sending notifications for draw {}: {}", code, e.getMessage());
            // No falla el sorteo si las notificaciones fallan
        }

        log.info("Draw executed successfully for code: {}", code);
        return DrawMapper.toDrawResponse(savedDraw);
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse getAssignment(String code, String email) {
        log.debug("Fetching assignment for code: {} and email: {}", code, email);

        Draw draw = drawRepository.findByCodeWithParticipants(code)
                .orElseThrow(() -> new DrawNotFoundException(code));

        // Verificar que el sorteo fue ejecutado
        if (draw.getStatus() == DrawStatus.PENDING) {
            return AssignmentResponse.notDrawnYet(draw.getName());
        }

        // Buscar participante
        Participant participant = draw.getParticipants().stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (participant == null || participant.getAssignedTo() == null) {
            return AssignmentResponse.notFound();
        }

        Participant receiver = participant.getAssignedTo();

        return AssignmentResponse.builder()
                .drawName(draw.getName())
                .giverName(participant.getName())
                .giverEmail(participant.getEmail())
                .receiverName(receiver.getName())
                .receiverEmail(receiver.getEmail())
                .message("You are the Secret Santa for " + receiver.getName() + "!")
                .build();
    }

    @Override
    @Transactional
    public void resendNotifications(String code) {
        log.info("Resending notifications for draw: {}", code);

        Draw draw = drawRepository.findByCodeWithParticipants(code)
                .orElseThrow(() -> new DrawNotFoundException(code));

        if (draw.getStatus() == DrawStatus.PENDING) {
            throw new IllegalStateException("Cannot resend notifications for a draw that hasn't been executed");
        }

        notificationService.sendAllAssignments(draw);
        log.info("Notifications resent successfully for draw: {}", code);
    }
}