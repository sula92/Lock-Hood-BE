package com.t6.lockhood.controller;

import com.t6.lockhood.dto.UserInfoDTO;
import com.t6.lockhood.dto.UserLoginDTO;
import com.t6.lockhood.exceptions.ResourceNotFoundException;
import com.t6.lockhood.model.User;
import com.t6.lockhood.repository.UserRepository;
import com.t6.lockhood.utility.PasswordHasher;
import com.t6.lockhood.utility.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api",
        produces = "application/json")

@CrossOrigin(origins = {
        "*"

},
        allowedHeaders = "*",

        maxAge = 15 * 60,
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.DELETE,
                RequestMethod.PUT
        })
@Transactional
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordHasher passwordHasher;

    private final String adminPage="admin.html";
    private final String userPage="user.html";
    private final String errorPage="error.html";

    @PostMapping("/users/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO login(@RequestBody UserLoginDTO userLoginDTO){

        System.out.println("hhhhhhhhhhhhhhh");
        System.out.println(userLoginDTO.getUserName());

        List<User> users= userRepository.findAll();
        for (User user:users) {
            if (!user.getUserName().equalsIgnoreCase(userLoginDTO.getUserName()) ||
                    !passwordHasher.matches(userLoginDTO.getPassword(), user.getPassword())) {
                continue;
            }
            if(user.getPrivilege().equalsIgnoreCase("admin")){

                upgradeLegacyPassword(user, userLoginDTO.getPassword());
                Session.userId= String.valueOf(user.getId());
                Session.userName=user.getUserName();
                Session.privilege=user.getPrivilege();

                System.out.println(user.getPrivilege());

                return new UserInfoDTO(String.valueOf(user.getId()), adminPage, user.getPrivilege());
            }
            else if (user.getPrivilege().equalsIgnoreCase("user")){

                upgradeLegacyPassword(user, userLoginDTO.getPassword());
                Session.userId= String.valueOf(user.getId());
                Session.userName=user.getUserName();
                Session.privilege=user.getPrivilege();

                UserInfoDTO userInfoDTO= new UserInfoDTO(String.valueOf(user.getId()), userPage, user.getPrivilege());
                System.out.println("FFFFFFFFFFFFFFFFFFF"+userInfoDTO.getId());
                return userInfoDTO;
            }
        }
        return new UserInfoDTO("error", errorPage, "error");
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        System.out.println("yyyyyyyyyyyyyyyyyyyyy");
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id) throws ResourceNotFoundException {
        try {
            return userRepository.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("User is Not Exist in the DB");
        }
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody @Valid User user){
        user.setPassword(passwordHasher.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public User editUser(@PathVariable int id, @RequestBody @Valid User user){
        user.setPassword(resolvePasswordForUpdate(user));
        return userRepository.save(user);
    }

    private String resolvePasswordForUpdate(User user) {
        String incomingPassword = user.getPassword();
        String storedPassword = userRepository.findById(user.getId()).map(User::getPassword).orElse(null);
        boolean keepStored = storedPassword != null &&
                (incomingPassword.isBlank() || incomingPassword.equals(storedPassword));
        if (keepStored && passwordHasher.isHashed(storedPassword)) {
            return storedPassword;
        }
        return passwordHasher.encode(keepStored ? storedPassword : incomingPassword);
    }

    private void upgradeLegacyPassword(User user, String rawPassword) {
        if (!passwordHasher.isHashed(user.getPassword())) {
            user.setPassword(passwordHasher.encode(rawPassword));
            userRepository.save(user);
        }
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) throws ResourceNotFoundException {
        try {
            userRepository.delete(userRepository.findById(id).get());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("No Such User In The DB To Delete");
        }
    }


}
