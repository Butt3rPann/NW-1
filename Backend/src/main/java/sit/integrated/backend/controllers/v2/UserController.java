package sit.integrated.backend.controllers.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.integrated.backend.dtos.*;
import sit.integrated.backend.entities.AuthUserDetail;
import sit.integrated.backend.services.UserService;
import java.util.Objects;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/v2")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<BuyerResponseDto> getUserProfile(@PathVariable Integer id) {
        Integer tokenUserId = ((AuthUserDetail) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched");
        }

        return ResponseEntity.ok(userService.getUserProfileById(id)) ;

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<BuyerResponseDto> updateUserProfile(@PathVariable Integer id, @RequestBody UserProfileDto userProfileDto) {
        AuthUserDetail userDetail = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer tokenUserId = userDetail.getId();
        if (!Objects.equals(id, tokenUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Request user id not matched");
        }

        BuyerResponseDto updatedUser = userService.updateUserProfileById(id, userProfileDto);
        return ResponseEntity.ok(updatedUser);
    }
}
