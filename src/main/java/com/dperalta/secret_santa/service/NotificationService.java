package com.dperalta.secret_santa.service;

import com.dperalta.secret_santa.model.Draw;
import com.dperalta.secret_santa.model.Participant;

public interface NotificationService {

    /**
     * Envía notificación de asignación a un participante.
     *
     * @param giver    participante que da el regalo
     * @param receiver participante que recibe el regalo
     */
    void sendAssignment(Participant giver, Participant receiver);

    /**
     * Envía notificaciones a todos los participantes de un sorteo.
     *
     * @param draw sorteo
     */
    void sendAllAssignments(Draw draw);
}