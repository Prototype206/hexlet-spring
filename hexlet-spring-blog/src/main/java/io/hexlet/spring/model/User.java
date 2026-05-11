package io.hexlet.spring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
}
