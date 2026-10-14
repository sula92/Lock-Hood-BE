package com.t6.lockhood.controller;

import com.t6.lockhood.dto.UnitDTO;
import com.t6.lockhood.model.Department;
import com.t6.lockhood.model.Unit;
import com.t6.lockhood.repository.DepartmentRepository;
import com.t6.lockhood.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
public class UnitController {

        @Autowired
        UnitRepository unitRepository;

        @Autowired
        DepartmentRepository departmentRepository;

        @GetMapping("/units")
        List<Unit> getAll(){
                return unitRepository.findAll();
        }

        @GetMapping("/units/{id}")
        Unit getUnit(@PathVariable int id){
                return findUnit(id);
        }

        @PostMapping("/units")
        @ResponseStatus(HttpStatus.CREATED)
        Unit saveUnit(@RequestBody UnitDTO unitDTO){
                Unit unit=Unit.builder()
                        .name(unitDTO.getName())
                        .department(findDepartment(unitDTO.getDepartmentId())).build();
                return unitRepository.save(unit);
        }

        @PutMapping("/units/{id}")
        Unit editUnit(@PathVariable int id, @RequestBody UnitDTO unitDTO){
                findUnit(id);
                Unit unit=Unit.builder()
                        .id(id)
                        .name(unitDTO.getName())
                        .department(findDepartment(unitDTO.getDepartmentId())).build();
                return unitRepository.save(unit);
        }

        @DeleteMapping("/units/{id}")
        void deleteUnit(@PathVariable int id){
                findUnit(id);
                unitRepository.deleteById(id);
        }

        private Unit findUnit(int id){
                return unitRepository.findById(id).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit " + id + " not found"));
        }

        private Department findDepartment(int departmentId){
                return departmentRepository.findById(departmentId).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department " + departmentId + " not found"));
        }
}
