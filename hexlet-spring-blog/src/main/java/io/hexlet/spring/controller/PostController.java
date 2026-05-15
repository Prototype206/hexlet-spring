package io.hexlet.spring.controller;

import java.net.URI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    //private List<Post> posts = new ArrayList<>();

    //@Autowired
    private PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

// like deprecated
//    @GetMapping
//    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue="10") Integer limit){
//        List<Post> result = postRepository.findAll().stream().limit(limit).toList();
//        return ResponseEntity.ok()
//                .header("Total-Count", String.valueOf(postRepository.count()))
//                .body(result);
//    }

    @GetMapping(path="")
    public Page<Post> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection)
    {
        Pageable pageable = PageRequest.of(page, size, sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return postRepository.findByPublishedTrue(pageable);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        if(post.getTitle() == null || post.getTitle().trim().isEmpty() || post.getContent() == null || post.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .header("Error", "ValidationError")
                    .build();
        }
        postRepository.save(post);
        return ResponseEntity.created(URI.create("/posts")).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable String id) {
        var post = postRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        return post;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post update(@PathVariable String id, @Valid @RequestBody Post data) {
        var post = postRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());
        postRepository.save(post);
        return post;

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable String id) {
        Post post = postRepository.findById(Long.valueOf(id))
            .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        postRepository.deleteById(Long.valueOf(id));
    }
}
