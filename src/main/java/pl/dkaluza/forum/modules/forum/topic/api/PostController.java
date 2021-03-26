package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.forum.topic.api.hateoas.PostModelAssembler;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreatePostModel;
import pl.dkaluza.forum.modules.forum.topic.services.PostService;

import javax.validation.Valid;

@RestController
public class PostController {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final PostPrivilegesChecker postPrivilegesChecker;
    private final PostService postService;
    private final PostModelAssembler postModelAssembler;
    private final PagedResourcesAssembler<PostModel> pagedPostModelsAssembler;

    @Autowired
    public PostController(PostPrivilegesChecker postPrivilegesChecker, PostService postService, PostModelAssembler postModelAssembler, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PagedResourcesAssembler<PostModel> pagedPostModelsAssembler) {
        this.postPrivilegesChecker = postPrivilegesChecker;
        this.postService = postService;
        this.postModelAssembler = postModelAssembler;
        this.pagedPostModelsAssembler = pagedPostModelsAssembler;
    }

    @PostMapping("/post")
    @PreAuthorize("@postPrivilegesChecker.canCreatePost(#auth, #model)")
    public PostModel create(Authentication auth, @RequestBody @Valid CreatePostModel model) {
        return postModelAssembler.toModel(
            postService.create(model)
        );
    }

    @GetMapping("/post")
    public PagedModel<PostModel> getAll(@PageableDefault Pageable pageable) {
        return pagedPostModelsAssembler.toModel(
            postService.getAll(pageable), postModelAssembler
        );
    }

    @GetMapping("/post/{id}")
    public PostModel get(@PathVariable("id") Long id) {
        return postModelAssembler.toModel(
            postService.get(id)
        );
    }
}
