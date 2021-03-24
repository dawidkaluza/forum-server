package pl.dkaluza.forum.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;

import javax.validation.Valid;

@RestController
public class LoginController {
    private final RequestErrorCreator requestErrorCreator;
    private final AuthenticationManager authManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public LoginController(RequestErrorCreator requestErrorCreator, AuthenticationManager authManager, JwtTokenUtil jwtTokenUtil) {
        this.requestErrorCreator = requestErrorCreator;
        this.authManager = authManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginModel authModel) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(authModel.getEmail(), authModel.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(userDetails.getId(), userDetails.getUsername()))
            .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgNotValidExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.UNAUTHORIZED)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "security.authentication.badCredentials", "Bad credentials")
            .build();
    }
}
