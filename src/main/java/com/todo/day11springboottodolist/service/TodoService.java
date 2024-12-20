package com.todo.day11springboottodolist.service;

import com.todo.day11springboottodolist.exception.NotFoundException;
import com.todo.day11springboottodolist.model.Todo;
import com.todo.day11springboottodolist.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Integer id, Todo todo) {
        Todo todoToUpdate = todoRepository.findById(id).orElse(null);
        if (todoToUpdate == null) {
            throw new NotFoundException("Todo with the following id not found:" + id);
        }
        return todoRepository.save(todo);
    }

    public void delete(Integer id) {
        todoRepository.deleteById(id);
    }
}
