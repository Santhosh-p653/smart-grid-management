package com.smartgrid.controller;

import com.smartgrid.dto.AuthRequest;
import com.smartgrid.dto.AuthResponse;
import com.smartgrid.dto.RegisterRequest;
import com.smartgrid.entity.User;
import com.smartgrid.security.CustomUserDetails;
import com.smartgrid.security.JwtUtil;
import com.smartgrid.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully with ID: " + user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String roleStr = userDetails.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generateToken(userDetails.getUsername(), roleStr);

        return ResponseEntity.ok(new AuthResponse(
                token,
                userDetails.getUsername(),
                userDetails.getUser().getEmail(),
                userDetails.getUser().getFullName(),
                roleStr
        ));
    }
}
