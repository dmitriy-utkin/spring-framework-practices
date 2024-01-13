package ru.example.news.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.news.aop.PrivilegeValidation;
import ru.example.news.aop.ValidationType;
import ru.example.news.exception.EntityNotFoundException;
import ru.example.news.exception.UserAlreadyExistsException;
import ru.example.news.model.User;
import ru.example.news.repository.UserRepository;
import ru.example.news.service.UserService;
import ru.example.news.utils.BeanUtils;
import ru.example.news.web.model.defaults.FindAllSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll(FindAllSettings findAllSettings) {
        return userRepository.findAll(PageRequest.of(findAllSettings.getPageNum(), findAllSettings.getPageSize())).getContent();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @PrivilegeValidation(type = ValidationType.USER_FIND_BY_ID)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with ID " + id + " not found."));
    }

    @Override
    public User save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User with name " + user.getUsername() + " is already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    @PrivilegeValidation(type = ValidationType.USER_UPDATE)
    public User update(User user) {
        User existedUser = findById(user.getId());
        BeanUtils.copyNonNullProperties(user, existedUser);
        existedUser.setPassword(passwordEncoder.encode(existedUser.getPassword()));
        return userRepository.saveAndFlush(existedUser);
    }

    @Override
    @PrivilegeValidation(type = ValidationType.USER_DELETE)
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
    public boolean existsByUsername(String name) {
        return userRepository.existsByUsername(name);
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new EntityNotFoundException("User with name \"" + name + "\" not found"));
    }
}
