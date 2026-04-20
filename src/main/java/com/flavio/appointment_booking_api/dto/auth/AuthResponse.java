package com.flavio.appointment_booking_api.dto.auth;

public record AuthResponse(
        Long id,
        String name,
        String email,
        String role,
        String token,
        String message
) {
}