package pl.dkaluza.forum.modules.user.confirmRegistration.services;

import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;

public interface ConfirmRegistrationService {

    void confirmRegistration(ConfirmModel model);

    void resendToken(ResendTokenModel model);
}
