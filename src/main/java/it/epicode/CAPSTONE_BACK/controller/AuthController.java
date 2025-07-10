package it.epicode.CAPSTONE_BACK.controller;

import it.epicode.CAPSTONE_BACK.authentication.AuthService;
import it.epicode.CAPSTONE_BACK.enumeration.Role;
import lombok.RequiredArgsConstructor;
import it.epicode.CAPSTONE_BACK.model.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import it.epicode.CAPSTONE_BACK.payload.LoginRequest;
import it.epicode.CAPSTONE_BACK.payload.SignupRequest;
import it.epicode.CAPSTONE_BACK.payload.TokenResponse;
import it.epicode.CAPSTONE_BACK.service.JwtService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhosy:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@RequestBody @Valid SignupRequest req) {
        Utente u = authService.register(
                req.getUsername(),
                req.getPassword(),
                Role.valueOf(req.getRole().toUpperCase())
        );

        String token = jwtService.generateToken(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(token, u.getUsername(), u.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        Utente u = authService.authenticate(req.getUsername(), req.getPassword());

        String token = jwtService.generateToken(u);
        return ResponseEntity.ok(new TokenResponse(token, u.getUsername(), u.getRole().name()));
    }

}