package proj.bbs.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.post.IdGenerator;
import proj.bbs.post.PostMapper;
import proj.bbs.post.domain.Post;
import proj.bbs.post.repository.PostRepository;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.PostDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Transactional
    public long savePost(NewPostDTO postDTO, String email) {
        long id = IdGenerator.generate();
        User writer = userRepository.findByEmail(email);
        Post post = Post.createPost(id, postDTO, writer);
        postRepository.savePost(post);
        return id;
    }

    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId);
        return postMapper.postToPostDto(post);
    }

}
