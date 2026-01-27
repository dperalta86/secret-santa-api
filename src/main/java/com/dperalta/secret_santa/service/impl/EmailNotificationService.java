package com.dperalta.secret_santa.service.impl;

import com.dperalta.secret_santa.model.Draw;
import com.dperalta.secret_santa.model.Participant;
import com.dperalta.secret_santa.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendAssignment(Participant giver, Participant receiver) {
        log.info("Sending assignment notification to: {}", giver.getEmail());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(giver.getEmail());
            message.setSubject("🎁 Your Secret Santa Assignment - " + giver.getDraw().getName());
            message.setText(buildEmailContent(giver, receiver));

            log.debug("Sending email from: {} to: {}", fromEmail, giver.getEmail());

            mailSender.send(message);

            giver.setNotificationSent(true);
            giver.setNotificationSentAt(LocalDateTime.now());

            log.info("✅ Notification sent successfully to: {}", giver.getEmail());

        } catch (Exception e) {
            log.error("❌ Failed to send notification to {}: {}", giver.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public void sendAllAssignments(Draw draw) {
        log.info("Sending all assignments for draw: {}", draw.getCode());

        int successCount = 0;
        int failCount = 0;

        for (Participant participant : draw.getParticipants()) {
            if (participant.getAssignedTo() == null) {
                log.warn("Participant {} has no assignment, skipping notification", participant.getEmail());
                continue;
            }

            try {
                sendAssignment(participant, participant.getAssignedTo());
                successCount++;
            } catch (Exception e) {
                log.error("Failed to send notification to {}: {}", participant.getEmail(), e.getMessage());
                failCount++;
            }
        }

        log.info("Notification summary - Success: {}, Failed: {}", successCount, failCount);
    }

    private String buildEmailContent(Participant giver, Participant receiver) {
        Draw draw = giver.getDraw();

        StringBuilder content = new StringBuilder();
        content.append("Hello ").append(giver.getName()).append("!\n\n");
        content.append("Your Secret Santa assignment for '").append(draw.getName()).append("' is ready!\n\n");
        content.append("🎁 You are the Secret Santa for: ").append(receiver.getName()).append("\n");

        if (receiver.getEmail() != null) {
            content.append("📧 Email: ").append(receiver.getEmail()).append("\n");
        }

        if (draw.getBudgetLimit() != null) {
            content.append("💰 Budget limit: $").append(draw.getBudgetLimit()).append("\n");
        }

        if (draw.getDrawDate() != null) {
            String formattedDate = draw.getDrawDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
            content.append("📅 Event date: ").append(formattedDate).append("\n");
        }

        if (draw.getDescription() != null && !draw.getDescription().isEmpty()) {
            content.append("\nℹ️  Details: ").append(draw.getDescription()).append("\n");
        }

        content.append("\n");
        content.append("Remember to keep it a secret! 🤫\n\n");
        content.append("View your assignment online: ").append(frontendUrl)
                .append("/draw/").append(draw.getCode())
                .append("?email=").append(giver.getEmail()).append("\n\n");
        content.append("Happy gifting!\n");
        content.append("- Secret Santa Team");

        return content.toString();
    }
}