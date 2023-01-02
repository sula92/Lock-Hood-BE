package com.t6.lockhood.controller;

import com.t6.lockhood.dto.FactoryEmployeeCountDTO;
import com.t6.lockhood.dto.FactoryPerformedEmployeeDTO;
import com.t6.lockhood.exceptions.ResourceNotFoundException;
import com.t6.lockhood.model.Employee;
import com.t6.lockhood.model.EmployeeKPI;
import com.t6.lockhood.model.Factory;
import com.t6.lockhood.repository.EmployeeKPIRepository;
import com.t6.lockhood.repository.EmployeeRepository;
import com.t6.lockhood.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
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
public class FactoryController {

    @Autowired
    FactoryRepository factoryRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeKPIRepository employeeKPIRepository;

    

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/factories")
    public Factory saveFactory(@RequestBody Factory factory) throws Exception{


        return factoryRepository.save(factory);

    }

    @GetMapping("/factories")
    public List<Factory> getAllfactories(){
       return factoryRepository.findAll();
    }

    @GetMapping("/factories/counts")
    public FactoryEmployeeCountDTO getAllFactoryEmployeeCounts(){
        int a=0;
        int b=0;
        int c=0;
        int d=0;

        List<Employee> EmployeeList=employeeRepository.findAll();

        for (int i = 0; i <EmployeeList.size() ; i++) {
            if (EmployeeList.get(i).getFactory().getName().equalsIgnoreCase("A")){
                a++;
            }
            else if (EmployeeList.get(i).getFactory().getName().equalsIgnoreCase("B")){
                b++;
            }
            else {
                c++;
            }
        }

        return FactoryEmployeeCountDTO.builder().A(a).B(b).C(c).build();


    }

    @GetMapping("/factories/counts/{id}")
    public FactoryPerformedEmployeeDTO getAllFactoryPerformedEmployeeCounts(@PathVariable int id){
        int a=0;
        int b=0;
        int c=0;
        int d=0;

        List<EmployeeKPI> KPIList=employeeKPIRepository.getAllFactoryPerformedEmployees(id);

        for (int i = 0; i <KPIList.size() ; i++) {

            double avg=kpiAverage(KPIList.get(i).getAvailabilityLevel(), KPIList.get(i).getEfficiencyLevel(), KPIList.get(i).getReliabilityLevel());

            if (avg>=7.0){
                a++;
            }
            else if (avg<7.0 && avg>=5.0){
                b++;
            }
            else {
                c++;
            }
        }

        return FactoryPerformedEmployeeDTO.builder().high(a).medium(b).low(c).tot(a+b+c+d).build();


    }

    @GetMapping("/factories/{id}")
    public Factory getFactory(@PathVariable int id) throws Exception {


        Factory Factory= factoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Such Factory"));
        //Factory Factory= employeeRepository.findById(id).get();
        //Factory Factory= employeeRepository.getOne(id);
        return ResponseEntity.ok(Factory).getBody();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/factories/{id}")
    public Factory updateFactory(@PathVariable int id, @RequestBody @Valid Factory factory) throws Exception{
        Factory Factory1=factoryRepository.findById(id).get();
        Factory1.setName(factory.getName());
        Factory1.setDate(factory.getDate());


        return factoryRepository.save(Factory1);
    }

    @DeleteMapping("/factories/{id}")
    public void deleteFactory(@PathVariable int id) throws Exception {
        if (!(factoryRepository.findById(id).get()==null)) {
            factoryRepository.deleteById(id);
        }
        else {
            throw new ResourceNotFoundException("No Such Factory");
        }
    }

    private double kpiAverage(int eff, int avai, int rel){
        double avg=(eff+avai+rel)/3.0;
        return avg;
    }
    
    
}
