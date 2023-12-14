package com.example.fourth.service;

import com.example.fourth.model.User;
import com.example.fourth.web.model.defaults.FindAllSettings;

import java.util.List;

public interface UserService {

    List<User> findAll(FindAllSettings findAllSettings);
    User findById(Long id);
    User save(User user);
    List<User> saveAll(List<User> users);
    User update(User user);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    Long count();
    boolean existsByName(String name);
    User findByName(String name);
    User saveWithoutPrivilegeValidation(User user);
}
