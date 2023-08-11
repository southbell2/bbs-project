package proj.bbs.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import proj.bbs.user.controller.dto.UserRoleDTO;
import proj.bbs.user.service.UserService;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/admin/add-user-role")
    public ResponseEntity<?> addUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        userService.addUserRole(userRoleDTO.getUserId(), userRoleDTO.getRole());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/delete-user-role")
    public ResponseEntity<?> deleteUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        userService.deleteUserRole(userRoleDTO.getUserId(), userRoleDTO.getRole());
        return ResponseEntity.ok().build();
    }
}
