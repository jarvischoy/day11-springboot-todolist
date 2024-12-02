package com.todo.day11springboottodolist.repository;

import com.todo.day11springboottodolist.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
}
