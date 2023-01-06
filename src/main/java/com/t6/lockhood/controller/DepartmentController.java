package com.t6.lockhood.controller;

import com.t6.lockhood.dto.DepartmentDTO;
import com.t6.lockhood.model.Department;
import com.t6.lockhood.repository.DepartmentRepository;
import com.t6.lockhood.repository.FactoryRepository;
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
public class DepartmentController {


        @Autowired
        DepartmentRepository departmentRepository;

        @Autowired
        FactoryRepository factoryRepository;

        @GetMapping("/departments")
        List<Department> getAll(){
                return departmentRepository.findAll();
        }

        @GetMapping("/departments/{id}")
        Department getDepartment(@PathVariable int id){
                return departmentRepository.findById(id).get();
        }

        @PutMapping("/departments/{id}")
        Department editDepartment(@PathVariable int id, @RequestBody DepartmentDTO departmentDTO){
                departmentRepository.findById(id).get();
                Department department=Department.builder()
                        .id(departmentDTO.getId())
                        .name(departmentDTO.getName())
                        .factory(factoryRepository.findById(departmentDTO.getFactoryId()).get()).build();
                return departmentRepository.save(department);
        }

        @PostMapping("/departments")
        Department saveDepartment(@RequestBody DepartmentDTO departmentDTO){
                Department department=Department.builder()
                        .id(departmentDTO.getId())
                        .name(departmentDTO.getName())
                        .factory(factoryRepository.findById(departmentDTO.getFactoryId()).get()).build();
                return departmentRepository.save(department);
        }
}
