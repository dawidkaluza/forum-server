package pl.dkaluza.forum.modules.index.services;

import org.springframework.stereotype.Service;
import pl.dkaluza.forum.modules.index.models.IndexModel;

@Service
class IndexServiceImpl implements IndexService {
    @Override
    public IndexModel getIndex() {
        IndexModel model = new IndexModel();
        model.setMessage("Welcome to the discussion forum");
        return model;
    }
}
