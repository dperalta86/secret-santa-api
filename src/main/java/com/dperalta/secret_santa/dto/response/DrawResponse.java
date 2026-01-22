package com.dperalta.secret_santa.dto.response;

import com.dperalta.secret_santa.model.DrawStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private LocalDateTime drawDate;
    private BigDecimal budgetLimit;
    private DrawStatus status;
    private Integer participantCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}