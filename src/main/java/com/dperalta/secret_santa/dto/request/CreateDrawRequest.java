package com.dperalta.secret_santa.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class CreateDrawRequest {

    @NotBlank(message = "Draw name is required")
    @Size(min = 3, max = 255, message = "Draw name must be between 3 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Future(message = "Draw date must be in the future")
    private LocalDateTime drawDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Budget limit must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Budget limit must have at most 8 digits and 2 decimals")
    private BigDecimal budgetLimit;

    @NotNull(message = "Participants list is required")
    @Size(min = 3, message = "At least 3 participants are required for a secret santa draw")
    @Valid
    private List<ParticipantRequest> participants;
}