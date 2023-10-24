package org.example.service;

import org.example.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.repository.PostRepositoryStubImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepositoryStubImpl repository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostService(PostRepositoryStubImpl repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<PostDTO> all() {
        return repository.all().stream()
                .filter(post -> !post.isRemoved())
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public PostDTO getById(long id) {
        Post post = repository.getById(id)
                .filter(p -> !p.isRemoved())
                .orElseThrow(NotFoundException::new);
        return convertEntityToDto(post);
    }

    public PostDTO save(PostDTO post) {
        Post entity = convertDtoToEntity(post);
//        if (entity.isRemoved()) {
//            throw new NotFoundException();
//        }
        if (entity.getId() != 0 && repository.getById(entity.getId()).isPresent()) {
            Post existingPost = repository.getById(entity.getId()).get();
            if (existingPost.isRemoved()) {
                throw new NotFoundException();
            }
        }
        entity = repository.save(entity);
        return convertEntityToDto(entity);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }

    private PostDTO convertEntityToDto(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

    private Post convertDtoToEntity(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }
}