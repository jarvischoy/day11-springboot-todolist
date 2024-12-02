package com.todo.day11springboottodolist.service;

import com.todo.day11springboottodolist.model.Todo;
import com.todo.day11springboottodolist.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoService todoService;


    @Test
    void should_return_the_given_todos_when_getAllTodos() {
        //given
        when(todoService.findAll()).thenReturn(List.of(new Todo(1, "Task 1", false)));
        //when
        List<Todo> allTodos = todoService.findAll();

        //then
        assertEquals(1, allTodos.size());
        assertEquals("Task 1", allTodos.get(0).getText());
        assertFalse(allTodos.get(0).isDone());
    }
}
