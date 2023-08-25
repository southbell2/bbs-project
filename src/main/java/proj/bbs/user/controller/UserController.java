package proj.bbs.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import proj.bbs.security.principal.UserPrincipal;
import proj.bbs.security.repository.TokenRepository;
import proj.bbs.user.service.dto.UserInfoDTO;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UpdatePasswordDTO;
import proj.bbs.user.service.dto.UpdateUserInfoDTO;

import static proj.bbs.constants.SecurityConstants.REFRESH_HEADER;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenRepository tokenRepository;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpUserDTO userDTO) {
        userService.signUp(userDTO);
        URI createdUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/userinfo")
            .build()
            .toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String refreshToken = getRefTokenFromCookie(request);
        if (StringUtils.hasText(refreshToken)) {
            tokenRepository.deleteToken(refreshToken);
        }

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
    public ResponseEntity<Void> updateUserInfo(@RequestBody @Valid UpdateUserInfoDTO userInfoDTO,
        Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        userService.updateUserInfo(userId, userInfoDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(Authentication authentication, @RequestBody @Valid
        UpdatePasswordDTO updatePasswordDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        userService.updatePassword(userId, updatePasswordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    private String getRefTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String refToken = null;
        if (cookies != null) {
            refToken = Arrays.stream(cookies)
                    .filter(cookie -> REFRESH_HEADER.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return refToken;
    }
}
