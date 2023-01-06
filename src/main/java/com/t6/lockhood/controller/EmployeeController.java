package com.t6.lockhood.controller;

import com.t6.lockhood.model.Employee;
import com.t6.lockhood.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
public class EmployeeController {

        @Autowired
        EmployeeRepository employeeRepository;


        @GetMapping("/employees")
        List<Employee> getAll(){
                return employeeRepository.findAll();
        }

        @GetMapping("/employees/{id}")
        Employee getEmployee(@PathVariable int id){
                return employeeRepository.findById(id).get();
        }
}
