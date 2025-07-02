package controller;


import authentication.AuthService;
import enumeration.Role;
import lombok.RequiredArgsConstructor;
import model.Utente;
import org.springframework.web.bind.annotation.*;
import payload.LoginRequest;
import payload.SignupRequest;
import payload.TokenResponse;
import util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public TokenResponse signup(@RequestBody SignupRequest req) {
        Utente u = authService.register(
                req.getUsername(),
                req.getPassword(),
                Role.valueOf(req.getRole().toUpperCase())
        );

        String token = jwtUtil.generateToken(u.getUsername());
        return new TokenResponse(token, u.getUsername(), u.getRole().name());
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {
        Utente u = authService.authenticate(req.getUsername(), req.getPassword());

        String token = jwtUtil.generateToken(u.getUsername());
        return new TokenResponse(token, u.getUsername(), u.getRole().name());
    }

    @GetMapping("/me")
    public Utente me(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwt);
        return authService.loadByUsername(username);
    }
}