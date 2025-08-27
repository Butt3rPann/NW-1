package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.dtos.UserSignInDto;
import sit.integrated.backend.services.UserService;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto> login(@ModelAttribute UserSignInDto userSigninDto) {
        UserResponseDto response = userService.loginUser(userSigninDto);
        return ResponseEntity.ok(response);
    }
}
