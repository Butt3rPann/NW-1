package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.UserRequestDto;
import sit.integrated.backend.dtos.UserResponseDto;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.UserService;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @PostMapping("/registers")
    public ResponseEntity<UserResponseDto> createUser(@ModelAttribute UserRequestDto user) {
        UserResponseDto userDto = userService.createUser(user);
        if ("SELLER".equalsIgnoreCase(user.getUserType()) && !user.getSellerNationalIdPhotos().isEmpty()) {
            fileService.storeNationalId(user.getSellerNationalIdPhotos(), userDto.getId());
            userDto.setSellerNationalIdPhotos(fileService.getSellerPhotos(userDto.getId()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
