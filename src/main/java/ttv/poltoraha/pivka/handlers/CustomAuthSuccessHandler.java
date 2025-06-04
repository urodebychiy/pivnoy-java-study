package ttv.poltoraha.pivka.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ttv.poltoraha.pivka.entity.MyUser;
import ttv.poltoraha.pivka.repository.MyUserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final MyUserRepository myUserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String username = authentication.getName();
        MyUser user = myUserRepository.findByUsername(username);

        if (user.isMigratedUser()) {
            response.sendRedirect("/update-password");
        } else {
            response.sendRedirect("/login");
        }
    }
}
