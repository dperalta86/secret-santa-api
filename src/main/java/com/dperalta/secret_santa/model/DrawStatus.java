package com.dperalta.secret_santa.model;

public enum DrawStatus {
    PENDING,    // Sorteo creado, esperando ejecución
    DRAWN,      // Sorteo realizado, asignaciones hechas
    COMPLETED   // Sorteo finalizado (opcional, para futuro)
}