package com.green.springrestapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee implements Comparable<Employee>{
    int id;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("gender")
    String gender;

    @JsonProperty("department")
    String department;

    @JsonProperty("salary")
    double salary;

    @Override
    public int compareTo(Employee employee) {
        return employee.getFirstName().compareTo(this.getFirstName());
    }

    public static List<Employee> loadEmployee() {
        List<Employee> employees = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            employees = objectMapper.readValue(Employee.class.getResource("/employee.json"), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

}
