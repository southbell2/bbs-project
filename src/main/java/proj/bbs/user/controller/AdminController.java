package proj.bbs.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import proj.bbs.user.controller.dto.UserRoleDTO;
import proj.bbs.user.service.AdminService;
import proj.bbs.user.service.UserService;
import proj.bbs.user.service.dto.SignUpUserDTO;
import proj.bbs.user.service.dto.UserInfoAdminDTO;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @PostMapping("/admin/register-5f4dcc3b5aa765d61d8327deb882cf99")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpUserDTO userDTO) {
        adminService.signUpAdmin(userDTO);
        URI createdUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/userinfo")
                .build()
                .toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @PostMapping("/admin/add-user-role")
    public ResponseEntity<?> addUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        adminService.addUserRole(userRoleDTO.getUserId(), userRoleDTO.getRole());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/delete-user-role")
    public ResponseEntity<?> deleteUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        adminService.deleteUserRole(userRoleDTO.getUserId(), userRoleDTO.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/userinfo/{userId}")
    public ResponseEntity<UserInfoAdminDTO> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserInfoByAdmin(userId));
    }

    @DeleteMapping("/admin/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
