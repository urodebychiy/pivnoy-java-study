package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ttv.poltoraha.pivka.entity.MyUser;
import ttv.poltoraha.pivka.repository.MyUserRepository;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    private MyUserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String password, Principal principal) {
        MyUser user = userRepository.findByUsername(principal.getName());
        user.setPassword(passwordEncoder.encode(password));
        user.setMigratedUser(false);
        userRepository.save(user);
        return "redirect:/login";
    }
}
