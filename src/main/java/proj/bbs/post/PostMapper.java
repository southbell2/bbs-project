package proj.bbs.post;

import org.mapstruct.Mapper;
import proj.bbs.post.domain.Post;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.user.domain.User;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post newPostDtoToPost(NewPostDTO newPostDTO, User user, String nickname);

}
