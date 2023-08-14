package proj.bbs.user;

import org.mapstruct.Mapper;
import proj.bbs.user.domain.RoleType;
import proj.bbs.user.service.dto.PagedUserDTO;
import proj.bbs.user.service.dto.UserInfoAdminDTO;
import proj.bbs.user.service.dto.UserInfoDTO;
import proj.bbs.user.domain.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfoDTO userToUserInfoDto(User user);

    UserInfoAdminDTO userToUserInfoAdminDto(User user, List<RoleType> roles);

    PagedUserDTO userToPagedUserDto(User user, List<RoleType> roles);

}
