package com.dperalta.secret_santa.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "draws")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "draw_date")
    private LocalDateTime drawDate;

    @Column(name = "budget_limit", precision = 10, scale = 2)
    private BigDecimal budgetLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DrawStatus status = DrawStatus.PENDING;

    @OneToMany(mappedBy = "draw", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods para mantener la relación bidireccional
    public void addParticipant(Participant participant) {
        participants.add(participant);
        participant.setDraw(this);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
        participant.setDraw(null);
    }
}