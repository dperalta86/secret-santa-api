package com.dperalta.secret_santa.service;

import com.dperalta.secret_santa.dto.request.CreateDrawRequest;
import com.dperalta.secret_santa.dto.response.AssignmentResponse;
import com.dperalta.secret_santa.dto.response.DrawDetailResponse;
import com.dperalta.secret_santa.dto.response.DrawResponse;

public interface DrawService {

    /**
     * Crea un nuevo sorteo con participantes.
     *
     * @param request datos del sorteo
     * @return sorteo creado
     */
    DrawResponse createDraw(CreateDrawRequest request);

    /**
     * Obtiene información básica de un sorteo por código.
     *
     * @param code código del sorteo
     * @return información del sorteo
     */
    DrawResponse getDrawByCode(String code);

    /**
     * Obtiene información detallada de un sorteo incluyendo participantes.
     *
     * @param code código del sorteo
     * @return información detallada del sorteo
     */
    DrawDetailResponse getDrawDetailByCode(String code);

    /**
     * Ejecuta el sorteo (asigna amigos invisibles).
     *
     * @param code código del sorteo
     * @return sorteo actualizado
     */
    DrawResponse executeDraw(String code);

    /**
     * Obtiene la asignación de un participante específico.
     *
     * @param code  código del sorteo
     * @param email email del participante
     * @return asignación del participante
     */
    AssignmentResponse getAssignment(String code, String email);

    /**
     * Reenvía las notificaciones a todos los participantes.
     *
     * @param code código del sorteo
     */
    void resendNotifications(String code);
}