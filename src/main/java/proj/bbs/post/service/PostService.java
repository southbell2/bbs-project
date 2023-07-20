package proj.bbs.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.post.PostMapper;
import proj.bbs.post.domain.Post;
import proj.bbs.post.repository.PostRepository;
import proj.bbs.post.service.dto.NewPostDTO;
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
    public void savePost(NewPostDTO postDTO, String email) {
        User writer = userRepository.findByEmail(email);
        Post post = postMapper.newPostDtoToPost(postDTO, writer, writer.getNickname());
        postRepository.savePost(post);
    }

}
