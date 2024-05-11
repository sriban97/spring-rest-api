package com.green.springrestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.springrestapi.entity.Category;
import com.green.springrestapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CategoryController.class})
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryRepository categoryRepository;


    @Test
    public void save() throws Exception {
        Category category = Category.builder().catId(1).name("test").build();

        given(categoryRepository.save(any(Category.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        ResultActions resultActions = mockMvc.perform(post("/category/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)));
        resultActions.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.catId", is(category.getCatId())))
                .andExpect(jsonPath("$.name", is(category.getName())));

    }

    @Test
    public void findAll() throws Exception {

        List<Category> categories = List.of(Category.builder().catId(1).name("cat1").build(),
                Category.builder().catId(2).name("cat2").build(),
                Category.builder().catId(3).name("cat3").build(),
                Category.builder().catId(4).name("cat4").build(),
                Category.builder().catId(5).name("cat5").build());

        given(categoryRepository.findAll()).willReturn(categories);

        ResultActions resultActions = mockMvc.perform(get("/category/findAll"));
        resultActions.andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(categories.size())));

    }
}