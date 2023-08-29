package proj.bbs.post;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import proj.bbs.post.domain.Post;
import proj.bbs.post.service.dto.PagedPostDTO;
import proj.bbs.post.service.dto.PostDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T20:41:42+0900",
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

    @Override
    public PagedPostDTO postToPagedPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PagedPostDTO pagedPostDTO = new PagedPostDTO();

        pagedPostDTO.setId( post.getId() );
        pagedPostDTO.setViews( post.getViews() );
        pagedPostDTO.setTitle( post.getTitle() );
        pagedPostDTO.setCreatedAt( post.getCreatedAt() );
        pagedPostDTO.setNickname( post.getNickname() );

        return pagedPostDTO;
    }
}
