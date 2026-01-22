package com.dperalta.secret_santa.utils;

import com.dperalta.secret_santa.model.Participant;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DrawAlgorithmTest {

    @Test
    void testPerformDraw_WithValidParticipants_ShouldSucceed() {
        // Arrange
        List<Participant> participants = createParticipants(5);

        // Act
        Map<Participant, Participant> assignments = DrawAlgorithm.performDraw(participants);

        // Assert
        assertEquals(5, assignments.size());
        assertTrue(DrawAlgorithm.isValidDraw(assignments));

        // Verificar que nadie se regala a sí mismo
        assignments.forEach((giver, receiver) ->
                assertNotEquals(giver.getId(), receiver.getId())
        );
    }

    @Test
    void testPerformDraw_WithMinimumParticipants_ShouldSucceed() {
        // Arrange
        List<Participant> participants = createParticipants(3);

        // Act
        Map<Participant, Participant> assignments = DrawAlgorithm.performDraw(participants);

        // Assert
        assertTrue(DrawAlgorithm.isValidDraw(assignments));
    }

    @Test
    void testPerformDraw_WithInsufficientParticipants_ShouldThrow() {
        // Arrange
        List<Participant> participants = createParticipants(2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                DrawAlgorithm.performDraw(participants)
        );
    }

    @Test
    void testPerformDraw_CreatesCompleteLoop() {
        // Arrange
        List<Participant> participants = createParticipants(4);

        // Act
        Map<Participant, Participant> assignments = DrawAlgorithm.performDraw(participants);

        // Assert - verificar que todos están en el ciclo
        Set<Participant> visited = new HashSet<>();
        Participant current = participants.get(0);

        for (int i = 0; i < participants.size(); i++) {
            visited.add(current);
            current = assignments.get(current);
        }

        assertEquals(participants.size(), visited.size());
        assertEquals(participants.get(0), current); // Volvió al inicio
    }

    private List<Participant> createParticipants(int count) {
        List<Participant> participants = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            participants.add(Participant.builder()
                    .id((long) i)
                    .name("Person " + i)
                    .email("person" + i + "@test.com")
                    .build());
        }
        return participants;
    }
}