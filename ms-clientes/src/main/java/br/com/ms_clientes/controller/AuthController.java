package br.com.ms_clientes.controller;

import br.com.ms_clientes.dto.LoginDto;
import br.com.ms_clientes.repository.ClienteRepository;
import br.com.ms_clientes.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(ClienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto request) {
        var clienteOpt = repository.findByEmail(request.getEmail());

        if (clienteOpt.isPresent() && passwordEncoder.matches(request.getPassword(), clienteOpt.get().getPassword())) {
            String token = JwtUtil.generateToken(clienteOpt.get().getEmail());

            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, String>> validateToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        log.info("Validação de token recebida: {}", token);

        if (JwtUtil.validateToken(token)) {
            String email = JwtUtil.getUsername(token);
            log.info("Token válido! Usuário identificado: {}", email);
            return ResponseEntity.ok(Map.of("username", email));
        }

        log.warn("Token inválido ou expirado: {}", token);
        return ResponseEntity.status(401).build();
    }

}
