package proj.bbs.post.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {
    private Long id;
    private Long views;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;

}
