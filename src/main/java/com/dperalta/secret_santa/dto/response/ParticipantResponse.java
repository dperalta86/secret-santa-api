package com.dperalta.secret_santa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Boolean notificationSent;
    private LocalDateTime notificationSentAt;
    private LocalDateTime createdAt;
}