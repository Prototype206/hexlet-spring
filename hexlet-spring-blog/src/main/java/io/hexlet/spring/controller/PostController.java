package io.hexlet.spring.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.model.Post;

@RestController
public class PostController {
    private List<Post> posts = new ArrayList<>();

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue="10") Integer limit){
        return posts.stream().limit(limit).toList();
    }
    @PostMapping("/posts")
    public Post create(@RequestBody Post post) {
        if(post.getTitle() == null && post.getTitle().trim().isEmpty() || post.getContent() == null && post.getContent().trim().isEmpty()) {
            return post;
        }
        posts.add(post);
        return post;
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> show(@PathVariable String id) {
        var post = posts.stream()
            .filter(p -> p.getTitle().equals(id))
            .findFirst();
        return post;
    }

    @PutMapping("/posts/{id}") // Обновление страницы
    public Post update(@PathVariable String id, @RequestBody Post data) {
        var maybePost = posts.stream()
            .filter(p -> p.getTitle().equals(id))
            .findFirst();
        if (maybePost.isPresent()) {
            var post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setAuthor(data.getAuthor());
            post.setCreatedAt(LocalDateTime.now());
        }
        return data;
    }

    @DeleteMapping("/posts/{id}") // Удаление страницы
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getTitle().equals(id));
    }
}
