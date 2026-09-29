package springBoot.SpringBootMVC.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springBoot.SpringBootMVC.dto.UserUpdateDto;
import springBoot.SpringBootMVC.dto.UserDto;
import springBoot.SpringBootMVC.dto.PetDto;
import springBoot.SpringBootMVC.dto.UserUpdateDto;
import springBoot.SpringBootMVC.mapper.PetMapper;
import springBoot.SpringBootMVC.mapper.UserMapper;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.model.User;
import springBoot.SpringBootMVC.repository.UserRepository;
import springBoot.SpringBootMVC.service.UserService;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PetMapper petMapper;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserMapper userMapper, PetMapper petMapper, UserRepository userRepository) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.petMapper = petMapper;
        this.userRepository = userRepository;

    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(userMapper.map(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,@RequestBody UserUpdateDto userUpdateDto) {
        userService.updateUser(id, userUpdateDto);
        User user = userRepository.findUserById(id);
        UserDto userDto = userMapper.map(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> currentList = userService.getAll();
        List<UserDto> userDtoList = currentList.stream()
                .map(userMapper::map)
                .toList();
        return ResponseEntity.ok(userDtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@ Valid @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.notFound().build();
    }
}
