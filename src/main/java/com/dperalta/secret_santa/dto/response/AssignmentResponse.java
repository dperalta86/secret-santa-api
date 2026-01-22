package com.dperalta.secret_santa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponse {

    private String drawName;
    private String giverName;
    private String giverEmail;
    private String receiverName;
    private String receiverEmail;
    private String message;

    public static AssignmentResponse notDrawnYet(String drawName) {
        return AssignmentResponse.builder()
                .drawName(drawName)
                .message("The draw has not been executed yet. Please wait for the organizer to perform the draw.")
                .build();
    }

    public static AssignmentResponse notFound() {
        return AssignmentResponse.builder()
                .message("No assignment found. Please check your email or draw code.")
                .build();
    }
}