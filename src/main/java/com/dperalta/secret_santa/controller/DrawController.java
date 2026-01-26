package com.dperalta.secret_santa.controller;

import com.dperalta.secret_santa.dto.request.CreateDrawRequest;
import com.dperalta.secret_santa.dto.response.AssignmentResponse;
import com.dperalta.secret_santa.dto.response.DrawDetailResponse;
import com.dperalta.secret_santa.dto.response.DrawResponse;
import com.dperalta.secret_santa.service.DrawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/draws")
@RequiredArgsConstructor
@Tag(name = "Draw Management", description = "Endpoints for managing Secret Santa draws")
public class DrawController {

    private final DrawService drawService;

    @PostMapping
    @Operation(summary = "Create a new Secret Santa draw",
            description = "Creates a new draw with participants. Minimum 3 participants required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Draw created successfully",
                    content = @Content(schema = @Schema(implementation = DrawResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or insufficient participants"),
            @ApiResponse(responseCode = "409", description = "Duplicate participant email")
    })
    public ResponseEntity<DrawResponse> createDraw(
            @Valid @RequestBody CreateDrawRequest request) {

        log.info("Creating new draw: {}", request.getName());
        DrawResponse response = drawService.createDraw(request);
        log.info("Draw created successfully with code: {}", response.getCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get draw information",
            description = "Retrieves basic information about a draw by its unique code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Draw found",
                    content = @Content(schema = @Schema(implementation = DrawResponse.class))),
            @ApiResponse(responseCode = "404", description = "Draw not found")
    })
    public ResponseEntity<DrawResponse> getDrawByCode(
            @Parameter(description = "Unique draw code", example = "ABC123")
            @PathVariable String code) {

        log.info("Fetching draw with code: {}", code);
        DrawResponse response = drawService.getDrawByCode(code);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}/details")
    @Operation(summary = "Get draw details with participants",
            description = "Retrieves complete draw information including all participants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Draw details retrieved",
                    content = @Content(schema = @Schema(implementation = DrawDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Draw not found")
    })
    public ResponseEntity<DrawDetailResponse> getDrawDetails(
            @Parameter(description = "Unique draw code", example = "ABC123")
            @PathVariable String code) {

        log.info("Fetching draw details for code: {}", code);
        DrawDetailResponse response = drawService.getDrawDetailByCode(code);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{code}/execute")
    @Operation(summary = "Execute the Secret Santa draw",
            description = "Performs the random assignment and sends email notifications to all participants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Draw executed successfully",
                    content = @Content(schema = @Schema(implementation = DrawResponse.class))),
            @ApiResponse(responseCode = "404", description = "Draw not found"),
            @ApiResponse(responseCode = "409", description = "Draw already executed"),
            @ApiResponse(responseCode = "400", description = "Insufficient participants")
    })
    public ResponseEntity<DrawResponse> executeDraw(
            @Parameter(description = "Unique draw code", example = "ABC123")
            @PathVariable String code) {

        log.info("Executing draw with code: {}", code);
        DrawResponse response = drawService.executeDraw(code);
        log.info("Draw executed successfully: {}", code);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}/assignment")
    @Operation(summary = "Get participant assignment",
            description = "Retrieves the Secret Santa assignment for a specific participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment retrieved",
                    content = @Content(schema = @Schema(implementation = AssignmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Draw or participant not found"),
            @ApiResponse(responseCode = "400", description = "Draw not executed yet")
    })
    public ResponseEntity<AssignmentResponse> getAssignment(
            @Parameter(description = "Unique draw code", example = "ABC123")
            @PathVariable String code,

            @Parameter(description = "Participant email", example = "john@example.com")
            @RequestParam String email) {

        log.info("Fetching assignment for code: {} and email: {}", code, email);
        AssignmentResponse response = drawService.getAssignment(code, email);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{code}/resend-notifications")
    @Operation(summary = "Resend email notifications",
            description = "Resends assignment emails to all participants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications resent successfully"),
            @ApiResponse(responseCode = "404", description = "Draw not found"),
            @ApiResponse(responseCode = "400", description = "Draw not executed yet")
    })
    public ResponseEntity<Void> resendNotifications(
            @Parameter(description = "Unique draw code", example = "ABC123")
            @PathVariable String code) {

        log.info("Resending notifications for draw: {}", code);
        drawService.resendNotifications(code);
        log.info("Notifications resent successfully for draw: {}", code);

        return ResponseEntity.ok().build();
    }
}