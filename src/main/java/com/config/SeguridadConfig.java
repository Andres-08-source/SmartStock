package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta clase define el "encriptador" de contraseñas como un Bean de Spring,
  osea, un objeto q Spring crea una sola vez y puede inyectar (@Autowired)
  en cualquier otra clase que lo necesite (como AuthService).

  BCrypt es un algoritmo de hash diseño específicamente para contraseñas:
  es lento con el proposito de dificultar ataques de fuerza bruta y ademas genera
  un resultado distinto cada vez aunque la contraseña sea la misma,
  pero igual permite verificar si una contraseña coincide o no con su hash.
 */
@Configuration
public class SeguridadConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
