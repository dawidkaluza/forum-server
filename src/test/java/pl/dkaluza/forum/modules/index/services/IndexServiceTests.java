package pl.dkaluza.forum.modules.index.services;

import org.junit.jupiter.api.Test;
import pl.dkaluza.forum.modules.index.models.IndexModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexServiceTests {
    @Test
    public void getIndex_normalCall_returnModelWithWelcomeMessage() {
        //Given
        IndexService indexService = new IndexServiceImpl();
        
        //When
        IndexModel model = indexService.getIndex();

        //Then
        assertEquals(model.getMessage(), "Welcome to the discussion forum");
    }
}
