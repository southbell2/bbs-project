package proj.bbs.user;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.user.domain.User;
import proj.bbs.user.service.dto.SignUpUserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-07T08:09:26+0900",
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
}
