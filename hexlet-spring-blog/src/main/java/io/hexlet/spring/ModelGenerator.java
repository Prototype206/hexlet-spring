package io.hexlet.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;

@Component
public class ModelGenerator {

    @Autowired
    private Faker faker;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void generateData() {
        for(int i = 0; i < 8; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setBirthday(faker.timeAndDate().birthday());
            userRepository.save(user);

            Post post = new Post();
            post.setTitle(faker.book().title());
            post.setContent(faker.lorem().paragraph());
            post.setPublished(faker.bool().bool());
            postRepository.save(post);
        }
    }


}
