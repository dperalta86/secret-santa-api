package com.dperalta.secret_santa.utils;

import com.dperalta.secret_santa.dto.response.DrawDetailResponse;
import com.dperalta.secret_santa.dto.response.DrawResponse;
import com.dperalta.secret_santa.dto.response.ParticipantResponse;
import com.dperalta.secret_santa.model.Draw;
import com.dperalta.secret_santa.model.Participant;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DrawMapper {

    public static DrawResponse toDrawResponse(Draw draw) {
        return DrawResponse.builder()
                .id(draw.getId())
                .code(draw.getCode())
                .name(draw.getName())
                .description(draw.getDescription())
                .drawDate(draw.getDrawDate())
                .budgetLimit(draw.getBudgetLimit())
                .status(draw.getStatus())
                .participantCount(draw.getParticipants().size())
                .createdAt(draw.getCreatedAt())
                .updatedAt(draw.getUpdatedAt())
                .build();
    }

    public static DrawDetailResponse toDrawDetailResponse(Draw draw) {
        return DrawDetailResponse.builder()
                .id(draw.getId())
                .code(draw.getCode())
                .name(draw.getName())
                .description(draw.getDescription())
                .drawDate(draw.getDrawDate())
                .budgetLimit(draw.getBudgetLimit())
                .status(draw.getStatus())
                .participants(toParticipantResponseList(draw.getParticipants()))
                .createdAt(draw.getCreatedAt())
                .updatedAt(draw.getUpdatedAt())
                .build();
    }

    public static ParticipantResponse toParticipantResponse(Participant participant) {
        return ParticipantResponse.builder()
                .id(participant.getId())
                .name(participant.getName())
                .email(participant.getEmail())
                .phone(participant.getPhone())
                .notificationSent(participant.getNotificationSent())
                .notificationSentAt(participant.getNotificationSentAt())
                .createdAt(participant.getCreatedAt())
                .build();
    }

    public static List<ParticipantResponse> toParticipantResponseList(List<Participant> participants) {
        return participants.stream()
                .map(DrawMapper::toParticipantResponse)
                .collect(Collectors.toList());
    }
}