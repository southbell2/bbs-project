package proj.bbs.post;

import org.mapstruct.Mapper;
import proj.bbs.post.domain.Post;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.PagedPostDTO;
import proj.bbs.post.service.dto.PostDTO;
import proj.bbs.user.domain.User;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO postToPostDto(Post post);

    PagedPostDTO postToPagedPostDto(Post post);
}
