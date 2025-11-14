package com.pwc.todoworksapce.todo.service;

import com.pwc.todoworksapce.todo.controller.dto.RequestCreateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseCreateTodoDto;

/**
 * Todo 생성 서비스 - 생성 작업 담당
 */
public interface TodoCreateService {
    
    /**
     * 새로운 Todo 생성
     */
    ResponseCreateTodoDto createTodo(RequestCreateTodoDto requestCreateTodoDto);
}

