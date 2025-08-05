package com.project.image_service.controllers;

import com.project.image_service.dtos.AuthResponse;
import com.project.image_service.dtos.LoginRequest;
import com.project.image_service.dtos.SignupRequest;
import com.project.image_service.dtos.UserDTO;
import com.project.image_service.services.JwtService;
import com.project.image_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){
        if(userService.findByUsername(request.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        var user = userService.registerUser(request.getUsername(), request.getPassword());
        UserDTO userDTO = new UserDTO(user.getUsername());
        var token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(userDTO, token));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtService.generateToken(request.getUsername());
            UserDTO user = new UserDTO(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(user, token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
