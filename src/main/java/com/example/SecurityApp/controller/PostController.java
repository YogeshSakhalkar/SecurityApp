package com.example.SecurityApp.controller;


import com.example.SecurityApp.dto.PostDTO;
import com.example.SecurityApp.entites.PostEntity;
import com.example.SecurityApp.entites.User;
import com.example.SecurityApp.exception.ResourceNotFoundException;
import com.example.SecurityApp.repository.PostRepository;
import com.example.SecurityApp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostController(PostService postService,
                          PostRepository postRepository, ModelMapper modelMapper) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable Long postId) {

        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("post not found with id"+postId));
        return modelMapper.map(postEntity, PostDTO.class);
        //return postService.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost) {
        return postService.createNewPost(inputPost);
    }

}
