package io.hexlet.spring.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.model.Post;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private List<Post> posts = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue="10") Integer limit){
        List<Post> result = posts.stream().limit(limit).toList();
        return ResponseEntity.ok()
                .header("Total-Count", String.valueOf(posts.size()))
                .body(result);
    }
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        if(post.getTitle() == null || post.getTitle().trim().isEmpty() || post.getContent() == null || post.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .header("Error", "ValidationError")
                    .build();
        }
        posts.add(post);
        return ResponseEntity.created(URI.create("/posts")).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable String id) {
        var post = posts.stream()
            .filter(p -> p.getTitle().equals(id))
            .findFirst();
        if(post.isPresent()) {
            return ResponseEntity.ok().body(post.get());
        }
        return ResponseEntity.notFound().header("Error", "post with id = " + id + " not found").build();
    }

    @PutMapping("/{id}") // Обновление страницы
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream()
            .filter(p -> p.getTitle().equals(id))
            .findFirst();
        if (maybePost.isPresent()) {
            var post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setAuthor(data.getAuthor());
            post.setCreatedAt(LocalDateTime.now());
            return ResponseEntity.ok().body(data);
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}") // Удаление страницы
    public ResponseEntity<Void> destroy(@PathVariable String id) {
        boolean isDeleted = posts.removeIf(p -> p.getTitle().equals(id));
        if(!isDeleted) {
            return ResponseEntity.notFound().header("Error", "Post with id = " + id + " not found").build();
        }
        return ResponseEntity.noContent().header("Deleted", "post with id = " + id).build();
    }
}
