package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.dtos.UserSignInDto;
import sit.integrated.backend.services.AuthService;
import sit.integrated.backend.services.EmailService;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.UserService;
import sit.integrated.backend.utils.JwtUtils;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private FileService fileService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDto> createUser(@ModelAttribute UserRequestDto user) {
        UserResponseDto userDto = userService.createUser(user);
        if (user.getUserType().equals(Role.SELLER)) {
            List<MultipartFile> files = Arrays.asList(user.getIdCardImageFront(), user.getIdCardImageBack());
            fileService.storeNationalId(files, userDto.getId());
        }
        emailService.sendVertificationEmail(userDto.getEmail(), jwtUtils.generateEmailToken(userDto.getId(), userDto.getEmail(), userDto.getUserType()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/auth/verify-email")
    public ResponseEntity<UserResponseDto> verifyToken(@RequestParam String jwtToken) {
        jwtUtils.verifyToken(jwtToken);
        Map<String, Object> claims = jwtUtils.getJWTClaimsSet(jwtToken);
        if (jwtUtils.isExpired(claims)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token");
        }
        String email = claims.get("email").toString();
        Integer id = Integer.valueOf(claims.get("id").toString());
        UserResponseDto user = userService.getUserResponseDtoById(id);
        if (user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already active");
        }
        userService.updateStatus(email);
        user.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody UserSignInDto userSignInDto) {
        Map<String, Object> tokens = authService.authenticateUser(userSignInDto);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.get("refresh_token").toString())
                .httpOnly(true)
                .secure(true)
                .path("/nw1/itb-mshop/v2/auth/refresh")
                .maxAge(60 * 60 * 24)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(Map.entry("access_token", tokens.get("access_token")));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/nw1/itb-mshop/v2/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<Object> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
