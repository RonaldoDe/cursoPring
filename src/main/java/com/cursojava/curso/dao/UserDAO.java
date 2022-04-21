package com.cursojava.curso.dao;

import com.cursojava.curso.models.User;

import java.util.List;

public interface UserDAO {

    List<User> getUsers();

    void destroy(Long id);

    void store(User user);

    User verifyCredentials(User user);
}
