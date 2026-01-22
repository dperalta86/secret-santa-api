package com.dperalta.secret_santa.utils;

import com.dperalta.secret_santa.model.Participant;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;

@Slf4j
@UtilityClass
public class DrawAlgorithm {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Realiza el sorteo de amigo invisible usando un ciclo hamiltoniano.
     * Algoritmo:
     * 1. Mezcla aleatoriamente la lista de participantes
     * 2. Asigna cada participante al siguiente en la lista (circular)
     * <p>
     * Este enfoque garantiza que:
     * - Nadie se regala a sí mismo
     * - Todos dan y reciben exactamente un regalo
     * - Forma un ciclo cerrado (A→B→C→D→A)
     * - Siempre funciona en O(n) sin reintentos
     *
     * @param participants lista de participantes (mínimo 3)
     * @return mapa de asignaciones (giver → receiver)
     * @throws IllegalArgumentException si hay menos de 3 participantes
     */
    public static Map<Participant, Participant> performDraw(List<Participant> participants) {
        if (participants == null || participants.size() < 3) {
            throw new IllegalArgumentException("At least 3 participants are required for a secret santa draw");
        }

        log.info("Performing draw for {} participants using circular assignment", participants.size());

        // Crear una copia para no modificar la lista original
        List<Participant> shuffled = new ArrayList<>(participants);

        // Mezclar aleatoriamente
        Collections.shuffle(shuffled, RANDOM);

        // Crear asignaciones circulares
        Map<Participant, Participant> assignments = new HashMap<>();
        int n = shuffled.size();

        for (int i = 0; i < n; i++) {
            Participant giver = shuffled.get(i);
            Participant receiver = shuffled.get((i + 1) % n); // Circular: último → primero

            assignments.put(giver, receiver);
            log.debug("{} → {}", giver.getName(), receiver.getName());
        }

        log.info("Draw completed successfully. Created cycle of {} assignments", assignments.size());
        return assignments;
    }

    /**
     * Valida que las asignaciones formen un ciclo válido.
     * Útil para testing.
     *
     * @param assignments mapa de asignaciones
     * @return true si es válido
     */
    public static boolean isValidDraw(Map<Participant, Participant> assignments) {
        if (assignments == null || assignments.isEmpty()) {
            return false;
        }

        // Verificar que nadie se regala a sí mismo
        for (Map.Entry<Participant, Participant> entry : assignments.entrySet()) {
            if (entry.getKey().equals(entry.getValue())) {
                log.error("Invalid draw: {} assigned to themselves", entry.getKey().getName());
                return false;
            }
        }

        // Verificar que forme un ciclo completo
        Set<Participant> visited = new HashSet<>();
        Participant current = assignments.keySet().iterator().next();
        Participant start = current;

        do {
            if (visited.contains(current)) {
                log.error("Invalid draw: cycle detected before completing full loop");
                return false;
            }
            visited.add(current);
            current = assignments.get(current);
        } while (current != null && !current.equals(start));

        boolean isValid = visited.size() == assignments.size() && current != null;

        if (!isValid) {
            log.error("Invalid draw: incomplete cycle. Visited {}, expected {}",
                    visited.size(), assignments.size());
        }

        return isValid;
    }
}