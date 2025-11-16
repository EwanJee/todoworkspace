package com.pwc.todoworksapce.todo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import com.pwc.todoworksapce.todo.service.TodoDeleteService;

import lombok.RequiredArgsConstructor;

/**
 * Todo 삭제 서비스 구현체
 */
@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class TodoDeleteServiceImpl implements TodoDeleteService {

    private final TodoRepository todoRepository;

    @Override
    @Transactional
    public void deleteTodo(Long id, Long requestMemberId) {
        Todo todo = findTodoById(id);
        validateDeletePermission(todo, requestMemberId);

        todoRepository.softDeleteById(id);
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CustomException("Todo를 찾을 수 없습니다."));
    }

    private void validateDeletePermission(Todo todo, Long requestMemberId) {
        if (!todo.isOwnedBy(requestMemberId)) {
            throw new CustomException("삭제 권한이 없습니다.");
        }
    }
}

