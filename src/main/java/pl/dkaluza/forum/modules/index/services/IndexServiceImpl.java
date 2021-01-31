package pl.dkaluza.forum.modules.index.services;

import org.springframework.stereotype.Service;
import pl.dkaluza.forum.modules.index.models.IndexModel;

@Service
public class IndexServiceImpl implements IndexService {
    @Override
    public IndexModel getIndex() {
        IndexModel model = new IndexModel();
        model.setMessage("Welcome on discussion forum server");
        return model;
    }
}
