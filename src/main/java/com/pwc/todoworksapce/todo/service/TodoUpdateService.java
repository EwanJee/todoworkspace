package com.pwc.todoworksapce.todo.service;

import com.pwc.todoworksapce.todo.controller.dto.RequestUpdatePriorityDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;

/**
 * Todo 수정 서비스 - 수정 작업 담당
 */
public interface TodoUpdateService {
    
    /**
     * Todo 수정
     */
    ResponseTodoDto updateTodo(Long id, RequestUpdateTodoDto requestUpdateTodoDto);
    
    /**
     * Todo 우선순위 변경
     */
    ResponseTodoDto updateTodoPriority(Long id, RequestUpdatePriorityDto requestUpdatePriorityDto);
}

