package proj.bbs.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.UpdatePostDTO;
import proj.bbs.user.domain.User;

@Entity
@Table(name = "posts")
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @Column(name = "post_id")
    private Long id;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long views;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    private String title;

    @Column(columnDefinition = "VARCHAR(5000) NOT NULL")
    private String content;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String nickname;

    public static Post createPost(long id, NewPostDTO newPostDTO, User user) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(newPostDTO.getTitle());
        post.setContent(newPostDTO.getContent());
        post.setUser(user);
        post.setNickname(user.getNickname());
        return post;
    }

    public void updatePost(UpdatePostDTO updatePostDTO) {
        setTitle(updatePostDTO.getTitle());
        setContent(updatePostDTO.getContent());
    }

    public void incrementViews() {
        views += 1;
    }

    private void setId(Long id) {
        if(id <= 0 ) throw new IllegalArgumentException("게시글의 id는 0보다 커야합니다.");
        this.id = id;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
