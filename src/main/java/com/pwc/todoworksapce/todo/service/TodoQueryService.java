package com.pwc.todoworksapce.todo.service;

import org.springframework.data.domain.Pageable;

import com.pwc.todoworksapce.todo.controller.dto.PageDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestTodoFilterDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;

/**
 * Todo 조회 서비스 - 읽기 전용 작업 담당
 */
public interface TodoQueryService {

    /**
     * ID로 Todo 조회
     */
    ResponseTodoDto getTodoById(Long id, Long requestMemberId);

    /**
     * 필터링과 정렬을 적용한 Todo 목록 조회 (페이지네이션)
     */
    PageDto<ResponseTodoDto> getAllTodos(RequestTodoFilterDto filterDto, Pageable pageable);
}
