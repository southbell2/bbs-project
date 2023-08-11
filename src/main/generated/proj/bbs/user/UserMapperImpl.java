package proj.bbs.user;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.user.controller.dto.UserInfoDTO;
import proj.bbs.user.domain.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-11T11:04:42+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

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
