package com.cursojava.curso.dao;

import com.cursojava.curso.models.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UserDAOImp implements UserDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<User> getUsers() {
        String query = "FROM User";
        List<User> users = entityManager.createQuery(query).getResultList();
        return users;
    }

    @Override
    public void destroy(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public void store(User user) {
        entityManager.merge(user);
    }

    @Override
    public User verifyCredentials(User user) {
        String query = "FROM User WHERE email = :email";
        List<User> loggerUser = entityManager.createQuery(query)
                .setParameter("email", user.getEmail())
                .getResultList();

        if(loggerUser.isEmpty()){
            return null;
        }

        String passwordHashed = loggerUser.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

         if(!argon2.verify(passwordHashed, user.getPassword())){
             return null;
         }

        return loggerUser.get(0);
    }


}
