package ttv.poltoraha.pivka.app.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import ttv.poltoraha.pivka.entity.MyUser;
import ttv.poltoraha.pivka.handlers.CustomAuthSuccessHandler;
import ttv.poltoraha.pivka.repository.MyUserRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomAuthSuccessHandlerTest {

    @Mock
    private MyUserRepository myUserRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;


    private CustomAuthSuccessHandler customAuthSuccessHandler;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customAuthSuccessHandler = new CustomAuthSuccessHandler(myUserRepository);
    }

    @Test
    void givenMigratedUser_whenLogin_thenRedirectToPasswordUpdate() throws Exception {

        String username = "testUser";
        MyUser user = new MyUser();
        user.setMigratedUser(true);


        when(authentication.getName()).thenReturn(username);
        when(myUserRepository.findByUsername(username)).thenReturn(user);


        customAuthSuccessHandler.onAuthenticationSuccess(request, response, authentication);


        verify(response).sendRedirect("/update-password");
    }

    @Test
    void givenRegularUser_whenLogin_thenRedirectToLogin() throws Exception {

        String username = "testUser";
        MyUser user = new MyUser();
        user.setMigratedUser(false);


        when(authentication.getName()).thenReturn(username);
        when(myUserRepository.findByUsername(username)).thenReturn(user);


        customAuthSuccessHandler.onAuthenticationSuccess(request, response, authentication);


        verify(response).sendRedirect("/login");
    }
}
