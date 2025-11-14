package com.pwc.todoworksapce.todo.repository;

import com.pwc.todoworksapce.todo.entity.TodoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {

    @Modifying
    @Query("DELETE FROM TodoTag tt WHERE tt.todo.id = :todoId")
    void deleteByTodoId(@Param("todoId") Long todoId);
}
