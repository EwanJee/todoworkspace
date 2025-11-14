package com.pwc.todoworksapce.todo.service;

/**
 * Todo 삭제 서비스 - 삭제 작업 담당
 */
public interface TodoDeleteService {
    
    /**
     * Todo 삭제 (Soft Delete)
     */
    void deleteTodo(Long id, Long requestMemberId);
}

