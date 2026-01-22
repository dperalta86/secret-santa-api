package com.dperalta.secret_santa.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class CodeGenerator {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 6;

    /**
     * Genera un código alfanumérico aleatorio.
     * Usa SecureRandom para mejor distribución y seguridad.
     *
     * @param length longitud del código
     * @return código generado
     */
    public static String generate(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }
        return code.toString();
    }

    /**
     * Genera un código de longitud por defecto (6 caracteres).
     *
     * @return código generado
     */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    /**
     * Genera un código único verificando que no exista en la base de datos.
     *
     * @param existsChecker función que verifica si el código existe
     * @param maxAttempts   máximo de intentos antes de fallar
     * @return código único generado
     * @throws IllegalStateException si no se puede generar un código único
     */
    public static String generateUnique(java.util.function.Predicate<String> existsChecker, int maxAttempts) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            String code = generate();
            if (!existsChecker.test(code)) {
                return code;
            }
        }
        throw new IllegalStateException("Failed to generate unique code after " + maxAttempts + " attempts");
    }

    /**
     * Genera un código único con 10 intentos por defecto.
     */
    public static String generateUnique(java.util.function.Predicate<String> existsChecker) {
        return generateUnique(existsChecker, 10);
    }
}