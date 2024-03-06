package com.in28minutes.res.webservices.restfulwebservices.jpa;

import com.in28minutes.res.webservices.restfulwebservices.user.User;
import com.in28minutes.res.webservices.restfulwebservices.user.UserDaoService;
import com.in28minutes.res.webservices.restfulwebservices.user.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJpaController {

    private UserRepository repository;
    private PostRepository postRepository;

    //Constructor injection
    @Autowired
    public UserJpaController(UserRepository repository,PostRepository postRepository) {
        this.repository = repository;
        this.postRepository=postRepository;
    }
    @GetMapping("jpa/users")
    public List<User> retrieveAllUsers(){
        return repository.findAll();
    }

    @GetMapping("jpa/users/{id}")
    public User findOne(@PathVariable int id){
        Optional<User> user = repository.findById(id);

        if (user == null)
            throw new UserNotFoundException("id: " + id);

        return user.get();
    }

    @GetMapping("jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id){
        Optional<User> user = repository.findById(id);

           if (user.isEmpty())
               throw new UserNotFoundException("id: " + id);

        return user.get().getPosts();
   }

    @PostMapping("jpa/users/{id}/posts")
    public List<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post){
        Optional<User> user = repository.findById(id);

        if (user.isEmpty())
            throw new UserNotFoundException("id: " + id);

        post.setUser(user.get());

        postRepository.save(post);

        return user.get().getPosts();
    }

    @PostMapping("jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User saveduser = repository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveduser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        repository.deleteById(id);
    }
}
