package proj.bbs.user;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.service.dto.SignUpUserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T09:49:55+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User signUpDtoToUser(SignUpUserDTO signUpUserDTO) {
        if ( signUpUserDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( signUpUserDTO.getEmail() );
        user.password( signUpUserDTO.getPassword() );
        user.nickname( signUpUserDTO.getNickname() );

        return user.build();
    }

    @Override
    public UserInfoDTO userToUserInfoDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();

        userInfoDTO.setEmail( user.getEmail() );
        userInfoDTO.setNickname( user.getNickname() );
        userInfoDTO.setCreatedAt( user.getCreatedAt() );

        return userInfoDTO;
    }
}
