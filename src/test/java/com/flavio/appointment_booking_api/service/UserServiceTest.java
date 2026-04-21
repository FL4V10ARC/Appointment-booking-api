package com.flavio.appointment_booking_api.service;

import com.flavio.appointment_booking_api.dto.auth.AuthResponse;
import com.flavio.appointment_booking_api.dto.auth.LoginRequest;
import com.flavio.appointment_booking_api.dto.auth.RegisterRequest;
import com.flavio.appointment_booking_api.entity.User;
import com.flavio.appointment_booking_api.enums.Role;
import com.flavio.appointment_booking_api.exception.BusinessException;
import com.flavio.appointment_booking_api.repository.UserRepository;
import com.flavio.appointment_booking_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "Flavio",
                "flavio@email.com",
                "123456"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");

        User savedUser = User.builder()
                .id(1L)
                .name(request.name())
                .email(request.email())
                .password("encoded-password")
                .role(Role.CLIENT)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AuthResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("Flavio", response.name());
        assertEquals("flavio@email.com", response.email());
        assertEquals("CLIENT", response.role());
        assertEquals("User registered successfully", response.message());

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.password());
    }

    @Test
    void shouldThrowExceptionWhenRegisteringWithExistingEmail() {
        RegisterRequest request = new RegisterRequest(
                "Flavio",
                "flavio@email.com",
                "123456"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.register(request));

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest(
                "flavio@email.com",
                "123456"
        );

        User user = User.builder()
                .id(1L)
                .name("Flavio")
                .email("flavio@email.com")
                .password("encoded-password")
                .role(Role.CLIENT)
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("fake-jwt-token");

        AuthResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("Flavio", response.name());
        assertEquals("flavio@email.com", response.email());
        assertEquals("CLIENT", response.role());
        assertEquals("fake-jwt-token", response.token());
        assertEquals("Login successful", response.message());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        LoginRequest request = new LoginRequest(
                "flavio@email.com",
                "wrong-password"
        );

        User user = User.builder()
                .id(1L)
                .name("Flavio")
                .email("flavio@email.com")
                .password("encoded-password")
                .role(Role.CLIENT)
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        assertThrows(BusinessException.class, () -> userService.login(request));

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest(
                "notfound@email.com",
                "123456"
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> userService.login(request));

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }
}