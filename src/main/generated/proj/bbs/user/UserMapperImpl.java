package proj.bbs.user;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.user.domain.User;
import proj.bbs.user.domain.UserRole;
import proj.bbs.user.service.dto.UserInfoAdminDTO;
import proj.bbs.user.service.dto.UserInfoDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-12T04:13:47+0900",
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

    @Override
    public UserInfoAdminDTO userToUserInfoAdminDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserInfoAdminDTO userInfoAdminDTO = new UserInfoAdminDTO();

        userInfoAdminDTO.setId( user.getId() );
        userInfoAdminDTO.setEmail( user.getEmail() );
        userInfoAdminDTO.setNickname( user.getNickname() );
        userInfoAdminDTO.setCreatedAt( user.getCreatedAt() );
        List<UserRole> list = user.getRoles();
        if ( list != null ) {
            userInfoAdminDTO.setRoles( new ArrayList<UserRole>( list ) );
        }

        return userInfoAdminDTO;
    }
}
