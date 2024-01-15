package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserJPA> listAllUsers(){
        return (List<UserJPA>) repository.findAll();
    }

    public UserJPA saveUser(UserJPA user){
        return repository.save(user);
    }

    public UserJPA findUserById(Long id){
        Optional<UserJPA> user = repository.findById(id);
        return user.orElse(null);
    }

    public void deleteUserById(Long id){
        // 1. найти студента для удаления
        Optional<UserJPA> deleted = repository.findById(id);
        // 2. если такой студент есть, то удалить его
        deleted.ifPresent(user -> repository.delete(user));
    }

    public UserJPA updateUser(Long id, UserJPA user){
        user.setId(id);
        //repository.delete(findStudentById(id));
        return saveUser(user);
    }
}
