package proj.bbs.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import proj.bbs.post.domain.Post;
import proj.bbs.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String nickname;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    private String content;

    @Column(columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Comment createComment(Long id, String content, Post post, User user) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setNickname(user.getNickname());
        return comment;
    }

    private void setId(Long id) {
        if (id <= 0) throw new IllegalArgumentException("게시글의 id는 0보다 커야합니다.");
        this.id = id;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setPost(Post post) {
        this.post = post;
    }

    private void setUser(User user) {
        this.user = user;
    }
}
