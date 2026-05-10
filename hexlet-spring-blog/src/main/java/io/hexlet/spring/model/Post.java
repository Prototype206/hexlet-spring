package io.hexlet.spring.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Post {
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
