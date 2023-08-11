package proj.bbs.user;

import org.mapstruct.Mapper;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.service.dto.SignUpUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfoDTO userToUserInfoDto(User user);
}
