//package com.example.bai4.controller;
//
//import com.example.bai4.entity.Role;
//import com.example.bai4.entity.User;
//import com.example.bai4.entity.AuthRequest;
//import com.example.bai4.entity.AuthResponse;
////import com.example.bai4.security.JwtService;
//import com.example.bai4.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Collections;
//import java.util.HashSet;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin("*")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsername(),
//                            loginRequest.getPassword()
//                    )
//            );
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Generate JWT token
//            String jwt = jwtService.generateToken(loginRequest.getUsername());
//
//            AuthResponse response = new AuthResponse();
//            response.setToken(jwt);
//            response.setEmail(loginRequest.getUsername());
//            response.setStatus("success");
//            response.setMessage("User logged in successfully");
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            AuthResponse response = new AuthResponse();
//            response.setStatus("error");
//            response.setMessage("Authentication failed: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
//        try {
//            if (userService.isEmailExists(request.getUsername())) {
//                AuthResponse response = new AuthResponse();
//                response.setStatus("error");
//                response.setMessage("Email is already taken!");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            User user = new User();
//            user.setEmail(request.getUsername());
//            user.setPassword(request.getPassword());
//
//            String result = userService.registerUser(user, "USER");
//
//            AuthResponse response = new AuthResponse();
//            if (result.contains("successfully")) {
//                // Generate JWT token for instant login
//                String jwt = jwtService.generateToken(request.getUsername());
//
//                response.setToken(jwt);
//                response.setEmail(request.getUsername());
//                response.setStatus("success");
//                response.setMessage("User registered successfully");
//                return ResponseEntity.ok(response);
//            } else {
//                response.setStatus("error");
//                response.setMessage(result);
//                return ResponseEntity.badRequest().body(response);
//            }
//        } catch (Exception e) {
//            AuthResponse response = new AuthResponse();
//            response.setStatus("error");
//            response.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//}