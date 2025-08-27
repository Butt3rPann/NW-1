package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.dtos.UserSignInDto;
import sit.integrated.backend.services.EmailService;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.UserService;
import sit.integrated.backend.utils.JwtUtils;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.TokenType;
import sit.integrated.backend.utils.UserStatus;

import java.util.Arrays;
import java.util.List;
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

    @PostMapping("/users/register")
    public ResponseEntity<UserResponseDto> createUser(@ModelAttribute UserRequestDto user) {
        UserResponseDto userDto = userService.createUser(user);
        if (user.getUserType().equals(Role.SELLER)) {
            List<MultipartFile> files = Arrays.asList(user.getIdCardImageFront(), user.getIdCardImageBack());
            fileService.storeNationalId(files, userDto.getId());
        }
        emailService.sendVertificationEmail(userDto.getEmail(), jwtUtils.generateToken(userDto.getId(), userDto.getEmail(), userDto.getUserType(), TokenType.EMAIL_TOKEN));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/users/verify-email")
    public ResponseEntity<UserResponseDto> verifyToken(@RequestParam String jwtToken) {
        jwtUtils.verifyToken(jwtToken);
        Map<String, Object> claims = jwtUtils.getJWTClaimsSet(jwtToken);
        if (jwtUtils.isExpired(claims)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token expired");
        }
        String email = claims.get("email").toString();
        Integer id = Integer.valueOf(claims.get("userId").toString());
        UserResponseDto user = userService.getUserById(id);
        if (user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already verified");
        }
        userService.updateStatus(email);
        user.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/authentications")
    public ResponseEntity<UserResponseDto> signInUser(@ModelAttribute UserSignInDto userSignInDto) {
        UserResponseDto response = userService.signInUser(userSignInDto);
        return ResponseEntity.ok(response);
    }
}
