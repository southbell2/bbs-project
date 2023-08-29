package proj.bbs.post.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PagedPostDTO {
    private Long id;
    private Long views;
    private String title;
    private LocalDateTime createdAt;
    private String nickname;
}
