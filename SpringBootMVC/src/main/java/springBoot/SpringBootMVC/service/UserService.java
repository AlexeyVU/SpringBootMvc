package springBoot.SpringBootMVC.service;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import springBoot.SpringBootMVC.controller.UserController;
import springBoot.SpringBootMVC.dto.PetDto;
import springBoot.SpringBootMVC.dto.UserDto;
import springBoot.SpringBootMVC.handler.ResourceNotFoundException;
import springBoot.SpringBootMVC.mapper.UserMapper;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.model.User;
import springBoot.SpringBootMVC.dto.UserUpdateDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.ILoggerFactory;
import springBoot.SpringBootMVC.repository.UserRepository;

@Service
public class UserService {

    //private final Map<Long, User> userMap;
    private final UserRepository userRepository;
    private final PetService petService;
    private int idCountUser;
    private int idCountPet;
    @Autowired
    private UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserService(@Lazy PetService petService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.petService = petService;
        this.idCountUser = 0;
        this.idCountPet = 0;
    }

    //Создание пользователя
    public UserDto createUser(UserDto userDto) {
        List<Pet> petList = petService.getAll();
        idCountUser++;
        Long id = (long) idCountUser;
        User newUser = new User(id, userDto.getName(), userDto.getEmail(), userDto.getAge(), petList);
        log.info("id " + newUser.getId());
        userRepository.save(id, newUser);
        return userMapper.map(newUser);
    }

    //Обновление пользователя
    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        User currentUser = userRepository.findUserById(id);
        if (currentUser == null) {
            throw new ResourceNotFoundException("User not found");
        }
        log.info("");
        userMapper.update(userUpdateDto, currentUser);
        userRepository.save(id,currentUser);
        return currentUser;
    }

    //Удаление пользователя
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    //Получение пользователя по id
    public User findUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    //получение списка пользователей
    public List<User> getAll() {
        return userRepository.getAll();
    }

}
