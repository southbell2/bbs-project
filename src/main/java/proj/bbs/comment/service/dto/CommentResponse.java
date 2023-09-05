package proj.bbs.comment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private String content;
    private String nickname;
}
