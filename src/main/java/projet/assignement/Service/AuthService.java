package projet.assignement.Service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import projet.assignement.DTOs.LoginRequest;
import projet.assignement.DTOs.LoginResponse;
import projet.assignement.DTOs.RegisterRequest;
import projet.assignement.Entities.User;
import projet.assignement.Repository.UserRepository;
import projet.assignement.Utils.JwtUtil;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepo.findByEmail(request.email).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "User already exists"));
        }

        String hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt());
        User user = new User();
        user.setEmail(request.email);
        user.setPasswordHash(hashedPassword);
        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered"));
    }

    public ResponseEntity<?> login(LoginRequest request) {
        Optional<User> userOpt = userRepo.findByEmail(request.email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }

        User user = userOpt.get();
        if (!BCrypt.checkpw(request.password, user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token, user));
    }
}

