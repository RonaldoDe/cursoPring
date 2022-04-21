package com.cursojava.curso.controllers;

import com.cursojava.curso.dao.UserDAO;
import com.cursojava.curso.models.User;
import com.cursojava.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping("/users")
    public List<User> getUser(@RequestHeader(value = "Authorization") String token){

        if(!validateToken(token)){
            return new ArrayList<>();
        }


        return userDAO.getUsers();
    }

    private boolean validateToken(String token){
        String userId = jwtUtil.getKey(token);

        return userId != null;

    }

    @PostMapping("/users")
    public void store(@RequestBody User user, @RequestHeader(value = "Authorization") String token){

        if(!validateToken(token)){
            return;
        }

        Argon2 argon = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon.hash(1, 1024, 1, user.getPassword());
        user.setPassword(hash);
        userDAO.store(user);
    }

    @DeleteMapping("/users/{id}")
    public void destroy(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){

        if(!validateToken(token)){
            return;
        }

        userDAO.destroy(id);
    }

}
