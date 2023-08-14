package proj.bbs.user.service.dto;

import lombok.Getter;
import lombok.Setter;
import proj.bbs.user.domain.RoleType;

import java.util.List;

@Setter
@Getter
public class PagedUserDTO {
    private Long id;
    private String email;
    private List<RoleType> roles;
}
