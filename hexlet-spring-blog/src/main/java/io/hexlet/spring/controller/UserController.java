package io.hexlet.spring.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<User> index(@RequestParam(defaultValue="10") Integer limit){
        return users.stream().limit(limit).toList();
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().header("Error", "The email address cannot be empty.").build();
        }
        users.add(user);
        return ResponseEntity.created(URI.create("/api/users")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean isDelete = users.removeIf(p -> String.valueOf(p.getId()).equals(id));
        if(isDelete) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.notFound().header("Error", "User with id = " + id + " not found").build();
    }
}
