package pl.dkaluza.forum.modules.index.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dkaluza.forum.modules.index.api.hateoas.IndexModelAssembler;
import pl.dkaluza.forum.modules.index.models.IndexModel;
import pl.dkaluza.forum.modules.index.services.IndexService;

@RestController
public class IndexController {
    private final IndexService indexService;
    private final IndexModelAssembler indexModelAssembler;

    @Autowired
    public IndexController(IndexService indexService, IndexModelAssembler indexModelAssembler) {
        this.indexService = indexService;
        this.indexModelAssembler = indexModelAssembler;
    }

    @GetMapping
    public IndexModel index() {
        return indexModelAssembler.toModel(
            indexService.getIndex()
        );
    }
}
