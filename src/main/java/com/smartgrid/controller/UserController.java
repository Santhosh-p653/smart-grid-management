package com.smartgrid.controller;

import com.smartgrid.dto.ChangePasswordRequest;
import com.smartgrid.dto.UserDto;
import com.smartgrid.entity.User;
import com.smartgrid.security.CustomUserDetails;
import com.smartgrid.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @Valid @RequestBody UserDto profileDto) {
        User updated = userService.updateProfile(userDetails.getUser().getId(), profileDto);
        // Refresh session object info
        userDetails.getUser().setEmail(updated.getEmail());
        userDetails.getUser().setFullName(updated.getFullName());
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userDetails.getUser().getId(), request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDto>> listUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserDto> users = userService.listUsers(search, pageable)
                .map(u -> new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole().getName().name()));
        
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
