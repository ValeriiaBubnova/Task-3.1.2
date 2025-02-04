package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserServiceImp(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public User findById(long id) {
        Optional<User> foundUser = usersRepository.findById(id);
        return foundUser.orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    @Transactional
    @Override
    public void save(User user) {
        usersRepository.save(user);
    }

    @Transactional
    @Override
    public void update(long id, User updatedUser) {
        updatedUser.setId(id);
        usersRepository.save(updatedUser);
    }

    @Transactional
    @Override
    public void delete(long id) {
        usersRepository.deleteById(id);
    }

}
