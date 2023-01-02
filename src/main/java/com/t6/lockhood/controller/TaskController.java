package com.t6.lockhood.controller;

import com.t6.lockhood.dto.TaskDTO;
import com.t6.lockhood.dto.TaskPriorityCountsDTO;
import com.t6.lockhood.model.Task;
import com.t6.lockhood.model.enums.Priority;
import com.t6.lockhood.repository.EmployeeRepository;
import com.t6.lockhood.repository.TaskRepository;
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
public class TaskController {

        @Autowired
        TaskRepository taskRepository;

        @Autowired
        EmployeeRepository employeeRepository;


        @GetMapping("/tasks")
        public List<Task> getAllTasks(){

                return taskRepository.findAll();
        }

        @GetMapping("/tasks/counts")
        public TaskPriorityCountsDTO getTasksCount(){

                int h=taskRepository.getTaskPriorityCounts("HIGH");
                int m=taskRepository.getTaskPriorityCounts("MEDIUM");
                int l=taskRepository.getTaskPriorityCounts("LOW");
                int com=taskRepository.getCompletedCount("100%");
                int pending=h+m+l-com;

               return TaskPriorityCountsDTO.builder().high(h).medium(m).low(l).completed(com).pending(pending)
                        .build();
        }


        @PutMapping("/tasks/{id}")
        public Task editTask(@PathVariable int id, @RequestBody TaskDTO taskDTO){

                System.out.println("XXXXXXXXXXXXXX"+employeeRepository.findById(taskDTO.getEmployeeId()).get());

                Task task=taskRepository.findById(id).get();
                task.setCompletion(taskDTO.getCompletion());
                task.setDescription(taskDTO.getDescription());
                task.setEmployees(employeeRepository.findById(taskDTO.getEmployeeId()).get());
                task.setFinalDate(taskDTO.getFinalDate());
                task.setStartingDate(taskDTO.getStartingDate());
                task.setPriority(Priority.valueOf(taskDTO.getPriority()));

                return taskRepository.save(task);
        }

        @PostMapping("/tasks")
        public Task createTask(@RequestBody TaskDTO taskDTO){

                Task task=Task.builder().
                        completion(taskDTO.
                                getCompletion()).
                        description(taskDTO.getDescription()).
                        finalDate(taskDTO.getFinalDate()).
                        startingDate(taskDTO.getStartingDate()).
                        priority(Priority.valueOf(taskDTO.getPriority())).
                        employees(employeeRepository.getReferenceById(taskDTO.getEmployeeId()))
                        .build();

                return taskRepository.save(task);
        }

        @DeleteMapping("/tasks/{id}")
        public void deleteTask(@PathVariable int id){

                taskRepository.deleteById(id);
        }

}
