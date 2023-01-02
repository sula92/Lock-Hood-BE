package com.t6.lockhood.controller;

import com.t6.lockhood.model.Unit;
import com.t6.lockhood.repository.UnitRepository;
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
public class UnitController {

        @Autowired
        UnitRepository unitRepository;

        @GetMapping("/units")
        List<Unit> getAll(){
                return unitRepository.findAll();
        }
}
