package com.example.auth.Service;

import com.example.auth.model.Entities.Auth;
import com.example.auth.model.Request.LoginRequest;
import com.example.auth.model.Request.RegisterRequest;
import com.example.auth.model.Entities.User;
import com.example.auth.repo.AuthRepo;
import com.example.auth.utils.JwtUtil;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthRepo authRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;


    public String register(RegisterRequest registerRequest){
        try {
            Auth auth = modelMapper.map(registerRequest, Auth.class);
            Auth authRequest = authRepo.save(auth);

            User user = modelMapper.map(registerRequest, User.class);
            user.setAuth(authRequest);
            userService.create(user);

            String token = jwtUtil.generateToken(user.getAuth().getEmail());
            return  token;
        }catch (DataAccessException e){
            throw new EntityExistsException(e);
        }
    }

    public String login(LoginRequest loginRequest){
        try {
            Optional<Auth> auth = authRepo.findById(loginRequest.getEmail());
            if (auth.isEmpty()) {
                throw new RuntimeException("user is not found");
            }
            if (!auth.get().getPassword().equals(loginRequest.getPassword())){
                throw new RuntimeException("Password is not match");
            }
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            return token;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
