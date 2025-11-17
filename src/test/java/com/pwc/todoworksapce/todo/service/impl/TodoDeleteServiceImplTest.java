package com.pwc.todoworksapce.todo.service.impl;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.entity.enums.Department;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.enums.Visibility;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TodoDeleteServiceImplTest {

    private TodoDeleteServiceImpl todoDeleteService;
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository = mock(TodoRepository.class);
        todoDeleteService = new TodoDeleteServiceImpl(todoRepository);
    }

    private void setMemberId(Member member, Long id) throws Exception {
        java.lang.reflect.Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, id);
    }

    @Test
    @DisplayName("Todo 삭제 성공")
    void deleteTodo_Success() throws Exception {
        // given
        Long todoId = 1L;
        Long memberId = 1L;
        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);

        Todo todo = Todo.builder()
                .id(todoId)
                .member(member)
                .title("테스트 제목")
                .content("테스트 내용")
                .visibility(Visibility.PUBLIC)
                .priority(BigDecimal.ONE)
                .build();

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));

        // when
        todoDeleteService.deleteTodo(todoId, memberId);

        // then
        verify(todoRepository).findByIdAndNotDeleted(todoId);
        verify(todoRepository).softDeleteById(todoId);
    }

    @Test
    @DisplayName("소유자가 아닌 사람이 Todo 삭제 시 예외 발생")
    void deleteTodo_WithoutPermission() throws Exception {
        // given
        Long todoId = 1L;
        Long ownerId = 1L;
        Long requestMemberId = 2L;
        Member owner = new Member("owner", "password", "nickname", Department.IT);
        setMemberId(owner, ownerId);

        Todo todo = Todo.builder()
                .id(todoId)
                .member(owner)
                .title("테스트 제목")
                .visibility(Visibility.PUBLIC)
                .build();

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));

        // when & then
        assertThatThrownBy(() -> todoDeleteService.deleteTodo(todoId, requestMemberId))
                .isInstanceOf(CustomException.class)
                .hasMessage("삭제 권한이 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 Todo 삭제 시 예외 발생")
    void deleteTodo_NotFound() {
        // given
        Long todoId = 999L;
        Long requestMemberId = 1L;

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> todoDeleteService.deleteTodo(todoId, requestMemberId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Todo를 찾을 수 없습니다.");
    }
}

