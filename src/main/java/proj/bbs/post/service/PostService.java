package proj.bbs.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.bbs.IdGenerator;
import proj.bbs.post.PostMapper;
import proj.bbs.post.domain.Post;
import proj.bbs.post.repository.PostRepository;
import proj.bbs.post.service.dto.NewPostDTO;
import proj.bbs.post.service.dto.PagedPostDTO;
import proj.bbs.post.service.dto.PostDTO;
import proj.bbs.post.service.dto.UpdatePostDTO;
import proj.bbs.user.domain.User;
import proj.bbs.user.repository.UserRepository;

import java.util.List;

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

    @Transactional
    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId);
        post.incrementViews();
        return postMapper.postToPostDto(post);
    }

    @Transactional
    public void updatePost(UpdatePostDTO updatePostDTO, Long postId) {
        Post post = postRepository.findById(postId);
        post.updatePost(updatePostDTO);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deletePost(postId);
    }

    public List<PagedPostDTO> getPagedPosts(Long beforeId, Integer limit) {
        List<Post> pagedPosts = postRepository.findPagedPosts(beforeId, limit);
        return pagedPosts.stream()
                .map(postMapper::postToPagedPostDto)
                .toList();
    }

    public List<PagedPostDTO> getPagedUserPosts(Long userId, int offset, int limit) {
        List<Post> pagedUserPosts = postRepository.findPagedUserPosts(userId, offset, limit);
        return pagedUserPosts.stream()
                .map(postMapper::postToPagedPostDto)
                .toList();
    }

}
