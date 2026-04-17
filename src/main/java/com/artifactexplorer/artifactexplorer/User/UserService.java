package com.artifactexplorer.User;

import org.springframework.stereotype.Service;

interface UserServiceInterface{
    User createUser(User user);
    User findUserByID(Long id);
}
@Service
public class UserService implements UserServiceInterface{
    private final UserRepository repository;
    public UserService(UserRepository repo){
        this.repository = repo;
    }
    @Override
    public User createUser(User user){
        return repository.save(user);
    }
    @Override
    public User findUserByID(Long id){
        return repository.findById(id).orElse(null);
    }
}
