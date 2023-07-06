package proj.bbs.user;

import org.mapstruct.Mapper;
import proj.bbs.user.domain.User;
import proj.bbs.user.service.dto.SignUpUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User signUpDtoToUser(SignUpUserDTO signUpUserDTO);
}
