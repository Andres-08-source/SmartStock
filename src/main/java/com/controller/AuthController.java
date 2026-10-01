package com.controller;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST http://localhost:8080/api/auth/registro
    // Body: { "nombre": "Bairon", "email": "bairon@correo.com", "password": "123456" }
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody AuthRequest request) {
        AuthResponse respuesta = authService.registrar(request);
        // Si el registro falló (ej: correo repetido), da error 400 en vez de 200
        return respuesta.isAutenticado()
                ? ResponseEntity.ok(respuesta)
                : ResponseEntity.badRequest().body(respuesta);
    }

    // POST http://localhost:8080/api/auth/login
    // Body: { "email": "bairon@correo.com", "password": "123456" }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse respuesta = authService.login(request);
        // Si la autenticación falla, devuelve error 401 (No autorizado) en vez de 200
        return respuesta.isAutenticado()
                ? ResponseEntity.ok(respuesta)
                : ResponseEntity.status(401).body(respuesta);
    }
}