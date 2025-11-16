package com.pwc.todoworksapce.todo.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.todo.controller.dto.PageDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestTodoFilterDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.enums.Visibility;
import com.pwc.todoworksapce.todo.mapper.TodoMapper;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import com.pwc.todoworksapce.todo.service.TodoQueryService;

import lombok.RequiredArgsConstructor;

/**
 * Todo 조회 서비스 구현체
 */
@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class TodoQueryServiceImpl implements TodoQueryService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    @Override
    public ResponseTodoDto getTodoById(Long id, Long requestMemberId) {
        Todo todo = findTodoById(id);
        validateReadPermission(todo, requestMemberId);
        return todoMapper.toResponseDto(todo);
    }

    @Override
    public PageDto<ResponseTodoDto> getAllTodos(RequestTodoFilterDto filterDto, Pageable pageable) {
        Long memberId = filterDto.memberId();
        String tagName = filterDto.tagName();
        Boolean isCompleted = filterDto.isCompleted();

        Page<Todo> todos = todoRepository.findByFiltersWithPagination(
                memberId, tagName, isCompleted, pageable
        );

        if (!todos.isEmpty()) {
            todoRepository.findTodosWithTags(todos.getContent());
        }

        Page<ResponseTodoDto> responsePage = todos.map(todoMapper::toResponseDto);
        return PageDto.of(responsePage);
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CustomException("Todo를 찾을 수 없습니다."));
    }

    private void validateReadPermission(Todo todo, Long requestMemberId) {
        if (todo.getVisibility() == Visibility.PRIVATE && !todo.isOwnedBy(requestMemberId)) {
            throw new CustomException("조회 권한이 없습니다.");
        }
    }
}
