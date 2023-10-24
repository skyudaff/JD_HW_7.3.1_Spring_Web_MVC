package org.example.controller;

import org.example.dto.PostDTO;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {
    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public List<PostDTO> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public PostDTO getById(@PathVariable long id) {
        return service.getById(id);
    }

    @PostMapping
    public PostDTO save(@RequestBody PostDTO post) {
        return service.save(post);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable long id) {
        service.removeById(id);
    }
}