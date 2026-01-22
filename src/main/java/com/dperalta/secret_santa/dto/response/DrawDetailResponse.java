package com.dperalta.secret_santa.dto.response;

import com.dperalta.secret_santa.model.DrawStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawDetailResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime drawDate;
    private BigDecimal budgetLimit;
    private DrawStatus status;
    private List<ParticipantResponse> participants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}