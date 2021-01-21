package pl.dkaluza.forum.modules.user.confirmRegistration.models;

import org.springframework.lang.NonNull;
import pl.dkaluza.forum.core.mappers.ModelMapper;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;

public class ConfirmRegistrationMapper implements ModelMapper<ConfirmRegistrationToken, ConfirmRegistrationModel> {
    @Override
    public ConfirmRegistrationModel toModel(@NonNull ConfirmRegistrationToken token) {
        ConfirmRegistrationModel model = new ConfirmRegistrationModel();
        model.setId(token.getId());
        model.setToken(token.getToken());
        model.setExpiresAt(token.getExpiresAt());
        return model;
    }
}
