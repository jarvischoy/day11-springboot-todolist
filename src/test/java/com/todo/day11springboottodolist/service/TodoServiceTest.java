package com.todo.day11springboottodolist.service;

import com.todo.day11springboottodolist.model.Todo;
import com.todo.day11springboottodolist.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void should_return_the_created_todo_when_create_given_a_todo() {
        //given
        Todo task1 = new Todo(1, "Task 1", false);
        when(todoRepository.save(task1)).thenReturn(task1);
        //when
        Todo createdTodo = todoService.create(task1);

        //then
        assertEquals("Task 1", createdTodo.getText());
        assertFalse(createdTodo.isDone());
    }

    @Test
    void should_return_the_updated_todo_when_update_given_a_todo() {
        //given
        Todo task1 = new Todo(1, "Task 1", false);
        when(todoRepository.findById(1)).thenReturn(Optional.of(task1));
        when(todoRepository.save(task1)).thenReturn(task1);
        //when
        Todo updatedTodo = todoService.update(1, task1);

        //then
        assertEquals("Task 1", updatedTodo.getText());
        assertFalse(updatedTodo.isDone());
    }

    @Test
    void should_return_null_when_update_given_a_todo_not_found() {
        //given
        Todo task1 = new Todo(1, "Task 1", false);
        when(todoRepository.findById(1)).thenReturn(Optional.empty());
        //when
        Todo updatedTodo = todoService.update(1, task1);

        //then
        assertNull(updatedTodo);
    }

    @Test
    void should_delete_todo_when_delete_given_a_todo_id() {
        //given
        Todo task1 = new Todo(1, "Task 1", false);
        when(todoRepository.save(task1)).thenReturn(task1);
        todoService.create(task1);


        //when
        todoService.delete(1);

        //then
        assertEquals(0, todoService.findAll().size());
    }

    @Test
    void should_return_null_when_delete_given_a_todo_id_not_found() {
        //given
        //when
        todoService.delete(1);

        //then
        assertNull(todoService.findById(1));
    }

}
