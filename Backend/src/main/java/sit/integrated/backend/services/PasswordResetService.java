package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.entities.PasswordReset;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.repositories.PasswordResetRepository;
import sit.integrated.backend.repositories.UserRepository;
import sit.integrated.backend.utils.JwtUtils;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PasswordResetService {
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    private static final long EXPIRATION_MINUTES = 15;

    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        passwordResetRepository.deleteByUser(user);
        String token = jwtUtils.generateResetToken(user.getId(), user.getEmail());

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUser(user);
        passwordReset.setResetToken(token);
        passwordReset.setExpiryDate(Instant.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES));
        passwordResetRepository.save(passwordReset);
        System.out.println("Password Reset Token: " + token);

        return token;
    }

    public void validateResetToken(String token) {
        jwtUtils.verifyToken(token);

        Optional<PasswordReset> resetOpt = passwordResetRepository.findByResetToken(token);
        if(resetOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token");

        PasswordReset passwordReset = resetOpt.get();

        if(passwordReset.isExpired()) {
            passwordResetRepository.delete(passwordReset);
        }
    }

    public void resetPassword(String token, String newPassword) {
        PasswordReset passwordReset = passwordResetRepository.findByResetToken(token).orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if(passwordReset.isExpired()) {
            passwordResetRepository.delete(passwordReset);
            throw new RuntimeException("Reset token expired");
        }

        User user = passwordReset.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetRepository.delete(passwordReset);
    }
}
