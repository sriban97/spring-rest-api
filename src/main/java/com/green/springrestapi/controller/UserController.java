package com.green.springrestapi.controller;

import com.green.springrestapi.entity.User;
import com.green.springrestapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin
public class UserController {
    @Autowired
    public UserRepository userRepository;


    @PostMapping(name = "save", path = "/save-all1")
    public ResponseEntity<String> saveAll1() {
        for (int i = 1; i <= 10000; i++) {
            User existsUser = User.builder().firstName(i + "-sriban").lastName(i + "-mari")
                    .email("sriban@gmail.com").phone("1125879653").password("123456").active('Y').build();
            userRepository.save(existsUser);
            System.out.println(existsUser);
        }
//        insert into APP_USER (ACTIVE,PASSWORD ,PHONE ,EMAIL ,FIRST_NAME ,LAST_NAME,ID  )
//        select ACTIVE,PASSWORD ,PHONE ,EMAIL ,FIRST_NAME ,LAST_NAME,(10000000+ID) from  APP_USER
        return new ResponseEntity<>("Save Success...", HttpStatus.CREATED);
    }

    @PostMapping(name = "save", path = "/save-all")
    public ResponseEntity<List<User>> saveAll(@RequestBody @Valid List<User> users) {
        List<User> response = userRepository.saveAll(users);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping(name = "save", path = "/save")
    public ResponseEntity<User> save(@RequestBody @Valid User user) {
        User response = userRepository.save(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(name = "update", path = "/update/{id}")
    public ResponseEntity<User> update(@RequestBody @Valid User user, @PathVariable long id) {
        User response = userRepository.findById(id).map(m -> {
            m.setFirstName(user.getFirstName());
            m.setLastName(user.getLastName());
            m.setPhone(user.getPhone());
            m.setEmail(user.getEmail());
            m.setPassword(user.getPassword());
            return userRepository.save(m);
        }).orElseGet(() -> userRepository.save(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(name = "get by id ", path = "/get/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") long id) {
        Optional<User> response = userRepository.findById(id);
        return response.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(name = "get all user", path = "/getAll")
//    @Cacheable(value = "todo-list", key = "'getList'")
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    @CacheEvict(value = "todo-list", key = "'getList'")
    @GetMapping(name = "clear catch", path = "/clr")
    public String catchCleared() {
        return "Catch Cleared...";
    }

    @GetMapping(name = "get active user", path = "/getActiveUser")
    public ResponseEntity<List<User>> getActiveUser() {
        Optional<List<User>> response = userRepository.getActiveUser();
        return response.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(name = "get all user name", path = "/all-user-name")
    public ResponseEntity<List<String>> getAllUserName() {
        List<String> list = userRepository.findAll().stream().map(m -> m.getLastName() + "-" + m.getFirstName()).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(name = "get all user name and role", path = "/all-user-name-role")
    public ResponseEntity<Map<String, List<User>>> getAllUserNameRole() {
        List<User> list = userRepository.findAll();
        Map<String, List<User>> map = list.stream().collect(Collectors.groupingBy(User::getRole, Collectors.toList()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}

//
//[
//        {
//        "id": 1,
//        "firstName": "sriban",
//        "lastName": "mari",
//        "email": "sriban@gmail.com",
//        "phone": "1234567891",
//        "password": "523",
//        "role": "A",
//        "active": "Y"
//        },
//        {
//        "id": 2,
//        "firstName": "mythili",
//        "lastName": "mari",
//        "email": "sriban@gmail.com",
//        "phone": "1234567891",
//        "password": "523",
//        "role": "S",
//        "active": "Y"
//        },
//        {
//        "id": 3,
//        "firstName": "jayamala",
//        "lastName": "mari",
//        "email": "sriban@gmail.com",
//        "phone": "1234567891",
//        "password": "523",
//        "role": "S",
//        "active": "Y"
//        },
//        {
//        "id": 4,
//        "firstName": "tamil",
//        "lastName": "mari",
//        "email": "sriban@gmail.com",
//        "phone": "1234567891",
//        "password": "523",
//        "role": "N",
//        "active": "Y"
//        }
//        ]