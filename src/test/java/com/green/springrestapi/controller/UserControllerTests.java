package com.green.springrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.springrestapi.entity.User;
import com.green.springrestapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenNewUserThenReturnCreatedUser() throws Exception {

        User user = User.builder().firstName("sriban").lastName("tamil")
                .email("sri@gmail.com").phone("1234569871").password("123456").build();

        given(userRepository.save(any(User.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(user.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())));
    }

    @Test
    public void givenExistingUserThenReturnUpdatedUser() throws Exception {
        User existsUser = User.builder().id(1).firstName("sriban").lastName("tamil")
                .email("sri@gmail.com").phone("1234569871").password("123456").build();


        User updateUser = User.builder().firstName("mari").lastName("tamil")
                .email("sri@gmail.com").phone("1234569871").password("123456").build();


        given(userRepository.findById(any(Long.class)))
                .willReturn(Optional.of(existsUser));

        given(userRepository.save(any(User.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/user/update/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUser)));

        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updateUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateUser.getLastName())))
                .andExpect(jsonPath("$.email", is(updateUser.getEmail())))
                .andExpect(jsonPath("$.phone", is(updateUser.getPhone())))
                .andExpect(jsonPath("$.password", is(updateUser.getPassword())));

    }

    @Test
    public void givenNewUserIntoUpdateWithThenCreateNewUserReturn() throws Exception {

        User newUser = User.builder().id(1).firstName("suman").lastName("s")
                .email("suman@gmail.com").phone("1125879653").password("123456").build();

        given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        ResultActions response = mockMvc.perform(put("/user/update/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));

        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(newUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newUser.getLastName())))
                .andExpect(jsonPath("$.email", is(newUser.getEmail())))
                .andExpect(jsonPath("$.phone", is(newUser.getPhone())))
                .andExpect(jsonPath("$.password", is(newUser.getPassword())));


    }

    @Test
    public void givenExistsUserIdThenReturnUser() throws Exception {

        User existsUser = User.builder().id(1).firstName("suman").lastName("s")
                .email("suman@gmail.com").phone("1125879653").password("123456").build();

        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(existsUser));

        ResultActions response = mockMvc.perform(get("/user/get/2").contentType(MediaType.APPLICATION_JSON));

        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(existsUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(existsUser.getLastName())))
                .andExpect(jsonPath("$.email", is(existsUser.getEmail())))
                .andExpect(jsonPath("$.phone", is(existsUser.getPhone())))
                .andExpect(jsonPath("$.password", is(existsUser.getPassword())));

    }

//    // JUnit test for Get All employees REST API
//    @Test
//    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
//        // given - precondition or setup
//        List<Employee> listOfEmployees = new ArrayList<>();
//        listOfEmployees.add(Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build());
//        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
//        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(get("/api/employees"));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.size()",
//                        is(listOfEmployees.size())));
//
//    }
//
//    // positive scenario - valid employee id
//    // JUnit test for GET employee by id REST API
//    @Test
//    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
//                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
//                .andExpect(jsonPath("$.email", is(employee.getEmail())));
//
//    }
//
//    // negative scenario - valid employee id
//    // JUnit test for GET employee by id REST API
//    @Test
//    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
//
//        // then - verify the output
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//
//    }
//    // JUnit test for update employee REST API - positive scenario
//        @Test
//        public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception{
//            // given - precondition or setup
//            long employeeId = 1L;
//            Employee savedEmployee = Employee.builder()
//                    .firstName("Ramesh")
//                    .lastName("Fadatare")
//                    .email("ramesh@gmail.com")
//                    .build();
//
//            Employee updatedEmployee = Employee.builder()
//                    .firstName("Ram")
//                    .lastName("Jadhav")
//                    .email("ram@gmail.com")
//                    .build();
//            given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
//            given(employeeService.updateEmployee(any(Employee.class)))
//                    .willAnswer((invocation)-> invocation.getArgument(0));
//
//            // when -  action or the behaviour that we are going test
//            ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .content(objectMapper.writeValueAsString(updatedEmployee)));
//
//
//            // then - verify the output
//            response.andExpect(status().isOk())
//                    .andDo(print())
//                    .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
//                    .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
//                    .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
//        }
//
//    // JUnit test for update employee REST API - negative scenario
//    @Test
//    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee savedEmployee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//
//        Employee updatedEmployee = Employee.builder()
//                .firstName("Ram")
//                .lastName("Jadhav")
//                .email("ram@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
//        given(employeeService.updateEmployee(any(Employee.class)))
//                .willAnswer((invocation)-> invocation.getArgument(0));
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedEmployee)));
//
//        // then - verify the output
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//// JUnit test for delete employee REST API
//    @Test
//    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        willDoNothing().given(employeeService).deleteEmployee(employeeId);
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print());
//    }
}