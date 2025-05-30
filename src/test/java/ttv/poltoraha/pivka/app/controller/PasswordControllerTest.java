package ttv.poltoraha.pivka.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ttv.poltoraha.pivka.controller.PasswordController;
import ttv.poltoraha.pivka.entity.MyUser;
import ttv.poltoraha.pivka.repository.MyUserRepository;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasswordControllerTest {

    @InjectMocks
    private PasswordController passwordController;

    @Mock
    private MyUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updatePassword_updatePasswordAndRedirect() {

        String newPassword = "newPassword123";
        MyUser user = new MyUser();
        user.setUsername("testUser");


        when(principal.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");


        String result = passwordController.updatePassword(newPassword, principal);


        verify(userRepository).findByUsername("testUser");
        verify(passwordEncoder).encode(newPassword);
        assertEquals("encodedPassword", user.getPassword());
        assertFalse(user.isMigratedUser());
        verify(userRepository).save(user);
        assertEquals("redirect:/login", result);
    }
}
