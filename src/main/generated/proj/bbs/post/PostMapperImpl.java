package proj.bbs.post;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.post.domain.Post;
import proj.bbs.post.service.dto.PostDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-25T18:58:06+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDTO postToPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setId( post.getId() );
        postDTO.setViews( post.getViews() );
        postDTO.setNickname( post.getNickname() );
        postDTO.setTitle( post.getTitle() );
        postDTO.setContent( post.getContent() );
        postDTO.setCreatedAt( post.getCreatedAt() );

        return postDTO;
    }
}
