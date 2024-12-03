package com.todo.day11springboottodolist.controller;

import com.todo.day11springboottodolist.model.Todo;
import com.todo.day11springboottodolist.repository.TodoRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TodoControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JacksonTester<List<Todo>> listJacksonTester;

    @Autowired
    private JacksonTester<Todo> todoJacksonTester;

    @BeforeEach
    void setUp() {
        giveDataToJpaRepository();
    }

    private void giveDataToJpaRepository() {
        todoRepository.deleteAll();
        todoRepository.save(new Todo(null, "Task 1", false));
        todoRepository.save(new Todo(null, "Task 2", false));
        todoRepository.save(new Todo(null, "Task 3", false));
        todoRepository.save(new Todo(null, "Task 4", false));
        todoRepository.save(new Todo(null, "Task 5", false));
    }

    @Test
    void should_return_todos_when_get_all_given_todos_exist() throws Exception {
        //given
        final List<Todo> givenTodos = todoRepository.findAll();

        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final List<Todo> todosResult = listJacksonTester.parse(jsonResponse).getObject();
        assertThat(todosResult)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(givenTodos);
    }

    @Test
    void should_return_todo_when_add_todo_given_todo() throws Exception {
        //given
        final Todo todo = new Todo(null, "Task 6", false);

        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.post("/todos")
                .contentType("application/json")
                .content(todoJacksonTester.write(todo).getJson()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();

        final Todo todoResult = todoJacksonTester.parse(jsonResponse).getObject();
        assertThat(todoResult)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(todo);
    }

    @Test
    void should_return_todo_when_update_todo_given_todo() throws Exception {
        //given
        final Todo todo = todoRepository.findAll().get(0);
        todo.setText("Task 1 Updated");

        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.put("/todos/" + todo.getId())
                .contentType("application/json")
                .content(todoJacksonTester.write(todo).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final Todo todoResult = todoJacksonTester.parse(jsonResponse).getObject();
        assertThat(todoResult)
                .usingRecursiveComparison()
                .isEqualTo(todo);
    }

    @Test
    void should_return_todo_when_remove_todo_given_todo_id() throws Exception {
        //given
        final Todo todo = todoRepository.findAll().get(0);

        //when
        //then
        client.perform(MockMvcRequestBuilders.delete("/todos/" + todo.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertThat(todoRepository.findById(todo.getId())).isEmpty();
    }

    @Test
    void should_throw_exception_when_update_todo_given_todo_id_not_found() throws Exception {
        //given
        final Todo notFoundTodo = new Todo(999, "Task 999", false);
        final int notFoundTodoId = notFoundTodo.getId();

        //when
        //then
        String contentAsString = client.perform(MockMvcRequestBuilders.put("/todos/" + notFoundTodoId)
                .contentType("application/json")
                .content(todoJacksonTester.write(notFoundTodo).getJson()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AssertionsForClassTypes.assertThat(contentAsString).isEqualTo("Todo with the following id not found:" + notFoundTodoId);
    }
}