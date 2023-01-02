package com.t6.lockhood.controller;

import com.t6.lockhood.dto.UserInfoDTO;
import com.t6.lockhood.dto.UserLoginDTO;
import com.t6.lockhood.exceptions.ResourceNotFoundException;
import com.t6.lockhood.model.User;
import com.t6.lockhood.repository.UserRepository;
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

    private final String adminPage="admin.html";
    private final String userPage="user.html";
    private final String errorPage="error.html";

    @PostMapping("/users/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDTO login(@RequestBody UserLoginDTO userLoginDTO){

        System.out.println("hhhhhhhhhhhhhhh");
        System.out.println(userLoginDTO.getUserName());
        System.out.println(userLoginDTO.getPassword());

        List<User> users= userRepository.findAll();
        for (User user:users) {
            if(user.getUserName().equalsIgnoreCase(userLoginDTO.getUserName()) &
                    user.getPassword().equalsIgnoreCase(userLoginDTO.getPassword()) &
                    user.getPrivilege().equalsIgnoreCase("admin")){

                Session.userId= String.valueOf(user.getId());
                Session.userName=user.getUserName();
                Session.privilege=user.getPrivilege();

                System.out.println(user.getPrivilege());

                return new UserInfoDTO(String.valueOf(user.getId()), adminPage, user.getPrivilege());
            }
            else if (user.getUserName().equalsIgnoreCase(userLoginDTO.getUserName()) &
                    user.getPassword().equalsIgnoreCase(userLoginDTO.getPassword()) &
                    user.getPrivilege().equalsIgnoreCase("user")){

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
        return userRepository.save(user);
    }

    @PutMapping("/users")
    public User editUser(@RequestBody @Valid User user){
        return userRepository.save(user);
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
