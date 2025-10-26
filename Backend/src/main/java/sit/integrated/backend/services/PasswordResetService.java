package sit.integrated.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.ResetPasswordRequest;
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

    @Transactional
    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        passwordResetRepository.deleteByUser(user);
        String token = jwtUtils.generateResetToken(user.getId(), user.getEmail());
        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setUser(user);
        passwordReset.setResetToken(token);
        passwordReset.setExpiryDate(Instant.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES));
        passwordResetRepository.save(passwordReset);
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

    public void resetPassword(ResetPasswordRequest request) {
        PasswordReset passwordReset = passwordResetRepository.findByResetToken(request.getToken()).orElseThrow(() -> new RuntimeException("Invalid reset token"));
        if (passwordReset.isExpired()) {
            passwordResetRepository.delete(passwordReset);
            throw new RuntimeException("Reset token expired");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password do not match");
        }
        User user = passwordReset.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
    }
}
