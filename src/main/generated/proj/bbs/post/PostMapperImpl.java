package proj.bbs.post;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.post.domain.Post;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.user.domain.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-14T11:42:32+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post newPostDtoToPost(NewPostDTO newPostDTO, User user, String nickname) {
        if ( newPostDTO == null && user == null && nickname == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        if ( newPostDTO != null ) {
            post.title( newPostDTO.getTitle() );
            post.content( newPostDTO.getContent() );
        }
        if ( user != null ) {
            post.id( user.getId() );
            post.createdAt( user.getCreatedAt() );
            post.nickname( user.getNickname() );
        }

        return post.build();
    }
}
