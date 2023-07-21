package proj.bbs.user.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import proj.bbs.config.UserPrincipal;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdatePasswordDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpUserDTO userDTO) {
        userService.signUp(userDTO);
        URI createdUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/userinfo")
            .build()
            .toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userinfo")
    public ResponseEntity<UserInfoDTO> userInfo(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        UserInfoDTO userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/update-userinfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserInfoDTO userInfoDTO,
        Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        userService.updateUserInfo(userId, userInfoDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(Authentication authentication, @RequestBody @Valid
        UpdatePasswordDTO updatePasswordDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        userService.updatePassword(userId, updatePasswordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hello")
    public ResponseEntity<?> helloController() {
        return ResponseEntity.ok("Hello Test!");
    }

}
