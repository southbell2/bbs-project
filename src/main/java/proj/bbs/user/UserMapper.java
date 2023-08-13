package proj.bbs.user;

import org.mapstruct.Mapper;
import proj.bbs.user.service.dto.UserInfoAdminDTO;
import proj.bbs.user.service.dto.UserInfoDTO;
import proj.bbs.user.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfoDTO userToUserInfoDto(User user);
    UserInfoAdminDTO userToUserInfoAdminDto(User user);
}
