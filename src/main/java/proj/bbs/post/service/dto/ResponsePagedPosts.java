package proj.bbs.post.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponsePagedPosts {
    List<PagedPostDTO> pagedPostDTOList;

    public ResponsePagedPosts(List<PagedPostDTO> pagedPostDTOList) {
        this.pagedPostDTOList = pagedPostDTOList;
    }
}
