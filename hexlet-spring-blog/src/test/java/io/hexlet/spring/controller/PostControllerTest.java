package io.hexlet.spring.controller;

import net.datafaker.Faker;
import tools.jackson.databind.ObjectMapper;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void testCreatePost() throws Exception {
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", "Test Post Title");
        postData.put("content", "This is the content of the test post");
        postData.put("published", false);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postData)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/posts"));

        Post savedPost = postRepository.findAll().get(0);
        assertThat(savedPost.getTitle()).isEqualTo("Test Post Title");
    }

    @Test
    void testGetAllPosts() throws Exception {
        Post post1 = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .supply(Select.field(Post::getTitle), () -> faker.lorem().characters(10, 45))
                .supply(Select.field(Post::getContent), () -> faker.lorem().characters(50, 200)) // Ограничили длину
                .set(Select.field(Post::isPublished), true)
                .create();

        Post post2 = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .supply(Select.field(Post::getTitle), () -> faker.lorem().characters(10, 45))
                .supply(Select.field(Post::getContent), () -> faker.lorem().characters(50, 200)) // Ограничили длину
                .set(Select.field(Post::isPublished), true)
                .create();

        postRepository.save(post1);
        postRepository.save(post2);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void testGetPostById() throws Exception {
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .supply(Select.field(Post::getTitle), () -> faker.lorem().characters(10, 45))
                .supply(Select.field(Post::getContent), () -> faker.lorem().characters(50, 200)) // Ограничили длину
                .create();
        postRepository.save(post);

        mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()));
    }

    @Test
    void testUpdatePost() throws Exception {
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .supply(Select.field(Post::getTitle), () -> faker.lorem().characters(10, 45))
                .supply(Select.field(Post::getContent), () -> faker.lorem().characters(50, 200)) // Ограничили длину
                .create();
        postRepository.save(post);

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("title", "Updated Title");
        updateData.put("content", "Updated content");
        updateData.put("published", true);

        mockMvc.perform(put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        Post updatedPost = postRepository.findById(post.getId()).get();
        assertThat(updatedPost.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testDeletePost() throws Exception {
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getCreatedAt))
                .ignore(Select.field(Post::getUpdatedAt))
                .supply(Select.field(Post::getTitle), () -> faker.lorem().characters(10, 45))
                .supply(Select.field(Post::getContent), () -> faker.lorem().characters(50, 200)) // Ограничили длину
                .create();
        postRepository.save(post);

        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent());

        assertThat(postRepository.findById(post.getId())).isEmpty();
    }
}