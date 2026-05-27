package io.hexlet.spring.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

//    public UserController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<User> index(@RequestParam(defaultValue="10") Integer limit){
        return userRepository.findAll().stream().limit(limit).toList();
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().header("Error", "The email address cannot be empty.").build();
        }
        userRepository.save(user);
        return ResponseEntity.created(URI.create("/api/users")).build();
    }


    @GetMapping("/{id}")
    public User show(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        if (userData.getFirstName() != null) {
            user.setFirstName(userData.getFirstName());
        }
        if (userData.getLastName() != null) {
            user.setLastName(userData.getLastName());
        }
        if (userData.getEmail() != null && !userData.getEmail().isEmpty()) {
            user.setEmail(userData.getEmail());
        }
        if (userData.getBirthday() != null) {
            user.setBirthday(userData.getBirthday());
        }

        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        User user = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        userRepository.deleteById(Long.valueOf(id));
    }
}
