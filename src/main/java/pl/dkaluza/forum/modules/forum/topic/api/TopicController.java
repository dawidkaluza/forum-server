package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.forum.topic.api.hateoas.PostModelAssembler;
import pl.dkaluza.forum.modules.forum.topic.api.hateoas.TopicModelAssembler;
import pl.dkaluza.forum.modules.forum.topic.models.basic.PostModel;
import pl.dkaluza.forum.modules.forum.topic.models.basic.TopicModel;
import pl.dkaluza.forum.modules.forum.topic.models.create.CreateTopicModel;
import pl.dkaluza.forum.modules.forum.topic.services.TopicService;

import javax.validation.Valid;

@RestController
public class TopicController {
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final TopicPrivilegesChecker topicPrivilegesChecker;
    private final TopicService topicService;
    private final TopicModelAssembler topicModelAssembler;
    private final PostModelAssembler postModelAssembler;
    private final PagedResourcesAssembler<TopicModel> pagedTopicModelsAssembler;
    private final PagedResourcesAssembler<PostModel> pagedPostModelsAssembler;

    @Autowired
    public TopicController(TopicPrivilegesChecker topicPrivilegesChecker, TopicService topicService, TopicModelAssembler topicModelAssembler, PostModelAssembler postModelAssembler, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PagedResourcesAssembler<TopicModel> pagedTopicModelsAssembler, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") PagedResourcesAssembler<PostModel> pagedPostModelsAssembler) {
        this.topicPrivilegesChecker = topicPrivilegesChecker;
        this.topicService = topicService;
        this.topicModelAssembler = topicModelAssembler;
        this.postModelAssembler = postModelAssembler;
        this.pagedTopicModelsAssembler = pagedTopicModelsAssembler;
        this.pagedPostModelsAssembler = pagedPostModelsAssembler;
    }

    @PostMapping("/topic")
    @PreAuthorize("@topicPrivilegesChecker.canCreateTopic(#auth, #model)")
    public TopicModel create(Authentication auth, @Valid @RequestBody CreateTopicModel model) {
        return topicModelAssembler.toModel(
            topicService.create(model)
        );
    }

    @GetMapping("/topic")
    public PagedModel<TopicModel> getAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return pagedTopicModelsAssembler.toModel(
            topicService.getAll(pageable), topicModelAssembler
        );
    }

    @GetMapping("/topic/{id}")
    public TopicModel get(@PathVariable("id") Long id) {
        return topicModelAssembler.toModel(
            topicService.get(id)
        );
    }

    @GetMapping("/topic/{id}/post")
    public PagedModel<PostModel> getPosts(@PathVariable("id") Long id, @PageableDefault Pageable pageable) {
        return pagedPostModelsAssembler.toModel(
            topicService.getPosts(id, pageable), postModelAssembler
        );
    }

    @PutMapping("/topic/{id}/close")
    @PreAuthorize("@topicPrivilegesChecker.canCloseTopic(#auth, #id)")
    public TopicModel close(@Nullable Authentication auth, @PathVariable("id") Long id) {
        return topicModelAssembler.toModel(
            topicService.close(id)
        );
    }

    @PutMapping("/topic/{id}/open")
    @PreAuthorize("@topicPrivilegesChecker.canOpenTopic(#auth, #id)")
    public TopicModel open(@Nullable Authentication auth, @PathVariable("id") Long id) {
        return topicModelAssembler.toModel(
            topicService.open(id)
        );
    }
}
