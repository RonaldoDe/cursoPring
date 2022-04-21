package com.cursojava.curso.controllers;

import com.cursojava.curso.dao.UserDAO;
import com.cursojava.curso.models.User;
import com.cursojava.curso.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class AuthController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody User user){
        User userLogged = userDAO.verifyCredentials(user);

        if(userLogged == null){
            return "FAIL";
        }

        String token = jwtUtil.create(String.valueOf(userLogged.getId()), userLogged.getEmail());

        return token;
    }

}
