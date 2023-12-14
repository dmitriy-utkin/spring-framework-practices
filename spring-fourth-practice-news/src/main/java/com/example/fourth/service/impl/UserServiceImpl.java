package com.example.fourth.service.impl;

import com.example.fourth.aop.IdLogger;
import com.example.fourth.aop.PrivilegeValidation;
import com.example.fourth.exception.EntityNotFoundException;
import com.example.fourth.exception.UserAlreadyExistsException;
import com.example.fourth.model.User;
import com.example.fourth.repository.UserRepository;
import com.example.fourth.service.UserService;
import com.example.fourth.utils.BeanUtils;
import com.example.fourth.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAll(FindAllSettings findAllSettings) {
        return userRepository.findAll(PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    @IdLogger
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with ID " + id + " not found."));
    }

    @Override
    @PrivilegeValidation
    @IdLogger
    public User save(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistsException("User with name " + user.getName() + " is already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User saveWithoutPrivilegeValidation(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistsException("User with name " + user.getName() + " is already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    @PrivilegeValidation
    @IdLogger
    public User update(User user) {
        User existedUser = findById(user.getId());
        BeanUtils.copyNonNullProperties(user, existedUser);
        return userRepository.save(existedUser);
    }

    @Override
    @PrivilegeValidation
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("User with name \"" + name + "\" not found"));
    }
}
