package proj.bbs.user;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.user.domain.RoleType;
import proj.bbs.user.domain.User;
import proj.bbs.user.service.dto.PagedUserDTO;
import proj.bbs.user.service.dto.UserInfoAdminDTO;
import proj.bbs.user.service.dto.UserInfoDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T20:41:42+0900",
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
    public UserInfoAdminDTO userToUserInfoAdminDto(User user, List<RoleType> roles) {
        if ( user == null && roles == null ) {
            return null;
        }

        UserInfoAdminDTO userInfoAdminDTO = new UserInfoAdminDTO();

        if ( user != null ) {
            userInfoAdminDTO.setId( user.getId() );
            userInfoAdminDTO.setEmail( user.getEmail() );
            userInfoAdminDTO.setNickname( user.getNickname() );
            userInfoAdminDTO.setCreatedAt( user.getCreatedAt() );
        }
        List<RoleType> list = roles;
        if ( list != null ) {
            userInfoAdminDTO.setRoles( new ArrayList<RoleType>( list ) );
        }

        return userInfoAdminDTO;
    }

    @Override
    public PagedUserDTO userToPagedUserDto(User user, List<RoleType> roles) {
        if ( user == null && roles == null ) {
            return null;
        }

        PagedUserDTO pagedUserDTO = new PagedUserDTO();

        if ( user != null ) {
            pagedUserDTO.setId( user.getId() );
            pagedUserDTO.setEmail( user.getEmail() );
        }
        List<RoleType> list = roles;
        if ( list != null ) {
            pagedUserDTO.setRoles( new ArrayList<RoleType>( list ) );
        }

        return pagedUserDTO;
    }
}
