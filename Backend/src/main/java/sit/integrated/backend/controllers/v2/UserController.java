package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.services.EmailService;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.UserService;
import sit.integrated.backend.utils.JwtUtils;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.TokenType;

import java.util.Map;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/registers")
    public ResponseEntity<UserResponseDto> createUser(@ModelAttribute UserRequestDto user) {
        UserResponseDto userDto = userService.createUser(user);
        if (user.getUserType().equals(Role.SELLER)) {
            fileService.storeNationalId(user.getSellerNationalIdPhotos(), userDto.getId());
            userDto.setSellerNationalIdPhotos(fileService.getSellerPhotos(userDto.getId()));
        }
        emailService.sendVertificationEmail(userDto.getEmail(), jwtUtils.generateToken(userDto.getEmail(), userDto.getUserType(), TokenType.EMAIL_TOKEN));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PatchMapping("/verify-email")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        jwtUtils.verifyToken(token);
        Map<String, Object> claims = jwtUtils.getJWTClaimsSet(token);
        if (jwtUtils.isExpired(claims)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token expired");
        }
        String email = claims.get("sub").toString();
        userService.updateStatus(email);
        return ResponseEntity.ok("Status updated successfully");
    }
}
