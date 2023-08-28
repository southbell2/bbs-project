package proj.bbs.post.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePostDTO {
    @NotBlank
    @Size(min = 1, max = 50, message = "제목의 크기는 1~50 사이여야 합니다")
    private String title;
    @NotBlank
    @Size(min = 1, max = 5000, message = ",내용의 크기는 1~5000 사이여야 합니다")
    private String content;

}
