package org.example.repository;

import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong postId = new AtomicLong(1);

    public List<Post> all() {
        return posts.values().stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id))
                .filter(post -> !post.isRemoved());
    }

    public Post save(Post post) {
        long id = post.getId();
        if (id == 0) {
            id = postId.getAndIncrement();
            post.setId(id);
        }
        if (posts.containsKey(id)) {
            Post existingPost = posts.get(id);
            if (existingPost.isRemoved()) {
                throw new NotFoundException();
            }
        }
        posts.put(id, post);
        return post;
    }

    public void removeById(long id) {
        Post post = posts.get(id);
        if (post != null) {
            post.setRemoved(true);
        }
    }
}
