package com.t6.lockhood.controller;


import com.t6.lockhood.dto.IncomeExpenseDTO;
import com.t6.lockhood.dto.TotIncomeAndExpensesDTO;
import com.t6.lockhood.exceptions.ResourceNotFoundException;
import com.t6.lockhood.model.*;
import com.t6.lockhood.model.Expense;
import com.t6.lockhood.repository.ExpenseRepository;
import com.t6.lockhood.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class IncomeAndExpenseController {
        
        @Autowired
        ExpenseRepository expenseRepository;

        @Autowired
        IncomeRepository incomeRepository;

        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/expenses")
        public Expense saveExpense(@RequestBody Expense expense) throws Exception{


                return expenseRepository.save(expense);

        }

        @GetMapping("/expenses")
        public List<Expense> getAllexpenses(){
                return expenseRepository.findAll();
        }
/*
        @GetMapping("/expenses/counts")
        public ExpenseEmployeeCountDTO getAllExpenseEmployeeCounts(){


                return null;


        }

        @GetMapping("/expenses/counts/{id}")
        public ExpensePerformedEmployeeDTO getAllExpensePerformedEmployeeCounts(@PathVariable int id){
                return null;
        }*/

        @ResponseStatus(HttpStatus.CREATED)
        @PutMapping("/expenses/{id}")
        public Expense updateExpense(@PathVariable int id, @RequestBody @Valid Expense expense) throws Exception{
                Expense Expense1=expenseRepository.findById(id).get();

                return expenseRepository.save(expense);
        }

        @DeleteMapping("/expenses/{id}")
        public void deleteExpense(@PathVariable int id) throws Exception {
                if (!(expenseRepository.findById(id).get()==null)) {
                        expenseRepository.deleteById(id);
                }
                else {
                        throw new ResourceNotFoundException("No Such Expense");
                }
        }

        //.....................................................................................


        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping("/incomes")
        public Income saveIncome(@RequestBody Income income) throws Exception{


                return incomeRepository.save(income);

        }

        @GetMapping("/incomes")
        public List<Income> getAlIncomes(){
                return incomeRepository.findAll();
        }

        @ResponseStatus(HttpStatus.CREATED)
        @PutMapping("/incomes/{id}")
        public Income updateIncome(@PathVariable int id, @RequestBody @Valid Income income) throws Exception{
                Income income1=incomeRepository.findById(id).get();

                return incomeRepository.save(income);
        }

        @DeleteMapping("/incomes/{id}")
        public void deleteIncome(@PathVariable int id) throws Exception {

                        incomeRepository.deleteById(id);

        }


        //..........................................................................................

        @GetMapping("/expense_records")
        public List<IncomeExpenseDTO> getExpenseRecords(){
                List<Expense> otherExpenses =expenseRepository.findAll();
                List<IncomeExpenseDTO> incomeExpenseDTOS=new ArrayList<>();

                otherExpenses.stream().forEach(otherExpense -> {
                        incomeExpenseDTOS.add(
                                IncomeExpenseDTO.builder().Amount(otherExpense.getAmount())
                                        .Date(otherExpense.getDate().toString())
                                        .description(otherExpense.getDescription()).build()
                        );
                });


                return incomeExpenseDTOS;
        }

        @GetMapping("/income_records")
        public List<IncomeExpenseDTO> getIncomeRecords(){
                List<Income> otherExpenses =incomeRepository.findAll();
                List<IncomeExpenseDTO> incomeExpenseDTOS=new ArrayList<>();

                otherExpenses.stream().forEach(otherExpense -> {
                        incomeExpenseDTOS.add(
                                IncomeExpenseDTO.builder().Amount(otherExpense.getAmount())
                                        .Date(otherExpense.getDate().toString())
                                        .description(otherExpense.getDescription()).build()
                        );
                });


                return incomeExpenseDTOS;
        }

        @GetMapping("/income&expense")
        public TotIncomeAndExpensesDTO getTotIncome(
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){

                LocalDate start = from != null ? from : LocalDate.now().withDayOfMonth(1);
                LocalDate end = to != null ? to : start.plusMonths(1).withDayOfMonth(1).minusDays(1);
                if (end.isBefore(start)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'to' must not be before 'from'");
                }

                Date fromDate = Date.valueOf(start);
                Date toExclusive = Date.valueOf(end.plusDays(1));

                long totIncome = valueOrZero(incomeRepository.getTotIncome(fromDate, toExclusive));
                long totSalaries = valueOrZero(expenseRepository.getTotSalaries(fromDate, toExclusive));
                long totOtherExp = valueOrZero(expenseRepository.getTotOtherExpenses(fromDate, toExclusive));
                long netIncome = totIncome - totSalaries - totOtherExp;

                TotIncomeAndExpensesDTO totIncomeAndExpensesDTO =new TotIncomeAndExpensesDTO();
                totIncomeAndExpensesDTO.setGrossIncome(String.valueOf(totIncome));
                totIncomeAndExpensesDTO.setNetIncome(String.valueOf(netIncome));
                totIncomeAndExpensesDTO.setSalariesPaid(String.valueOf(totSalaries));
                totIncomeAndExpensesDTO.setTotalOtherExpenses(String.valueOf(totOtherExp));

                return totIncomeAndExpensesDTO;
        }

        private static long valueOrZero(Long value) {
                return value == null ? 0L : value;
        }

}
