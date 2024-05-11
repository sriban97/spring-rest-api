package com.green.springrestapi.controller;

import com.green.springrestapi.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @GetMapping(path = "/getAll")
    public List<Employee> getEmployees() {
        return Employee.loadEmployee();
    }

    @GetMapping(path = {"/get/{dep}", "/get"})
    public String getEmp(@RequestParam(name = "dep", required = false) String dep) throws ExecutionException, InterruptedException {
        List<Employee> employees = Employee.loadEmployee();

        Predicate<Employee> predicate = (e) -> ObjectUtils.isEmpty(dep) || e.getDepartment().equals(dep);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("step 1 : " + Thread.currentThread().getName());
            for (int i = 0; i < 1000; i++) {
                employees.addAll(Employee.loadEmployee());
            }
            return employees.stream().filter(predicate).collect(Collectors.toList());
        }, executor).thenApplyAsync((emps) -> {
            System.out.println("step 2 : " + Thread.currentThread().getName());
            return emps.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.mapping(Employee::getSalary, Collectors.toList())));
        }, executor).thenApplyAsync((map) -> {
            System.out.println("step 3 : " + Thread.currentThread().getName());
            return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        }, executor).thenApplyAsync(doubleStream -> {
            System.out.println("step 4 : " + Thread.currentThread().getName());
            String out = doubleStream.stream().mapToDouble(Double::doubleValue).sum() + " : " + employees.stream().filter(predicate).mapToDouble(Employee::getSalary).sum();
            System.out.println();
            return out;
        }, executor);

        return "Success..." + future.get();
    }

    @GetMapping(path = "/orderEmp/v1")
    public ResponseEntity<List<Employee>> getOrderEmpV1() {
        List<Employee> employees =
                Employee.loadEmployee().stream().limit(100).sorted(Comparator.comparing(Employee::getDepartment).reversed().thenComparing(Employee::getGender)).collect(Collectors.toList());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(path = "/orderEmp/v2")
    public ResponseEntity<Employee[]> getOrderEmpV2() {
        List<Employee> employees =
                Employee.loadEmployee();
        Employee[] employees1 = new Employee[10];
        for (int i = 0; i < employees1.length; i++) {
            employees1[i] = employees.get(i);
        }
        Arrays.sort(employees1);

        return new ResponseEntity<>(employees1, HttpStatus.OK);
    }

    @GetMapping(path = "/orderEmp/v3")
    public ResponseEntity<List<Employee>> getOrderEmpV3() {
        Comparator<Employee> comparator = (e1, e2) -> {
            if (e1.getFirstName().equals(e2.getFirstName())) {
                return Double.compare(e1.getSalary(), e2.getSalary());
            }
            return e1.getFirstName().compareTo(e2.getFirstName());
        };
        List<Employee> employees =
                Employee.loadEmployee().stream().limit(100).sorted(comparator).collect(Collectors.toList());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

}
