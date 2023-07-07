package proj.bbs.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpUserDTO userDTO) {
        userService.signUp(userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userinfo")
    public ResponseEntity<UserInfoDTO> userInfo(Authentication authentication) {
        UserInfoDTO userInfo = userService.getUserInfo(authentication.getName());
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/update-userinfo")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserInfoDTO userInfoDTO) {
        userService.updateUserInfo(userInfoDTO);
        return ResponseEntity.ok().build();
    }

}
