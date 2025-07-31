package com.project.image_service.services;

import com.project.image_service.models.User;
import com.project.image_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Lazy private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User registerUser(String username, String password){
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder().username(username).password(encodedPassword).role("USER").build();

        return userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
