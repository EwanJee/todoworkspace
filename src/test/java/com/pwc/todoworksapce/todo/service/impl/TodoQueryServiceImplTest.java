package com.pwc.todoworksapce.todo.service.impl;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.entity.enums.Department;
import com.pwc.todoworksapce.todo.controller.dto.PageDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestTodoFilterDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.enums.Visibility;
import com.pwc.todoworksapce.todo.mapper.TodoMapper;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TodoQueryServiceImplTest {

    private TodoQueryServiceImpl todoQueryService;
    private TodoRepository todoRepository;
    private TodoMapper todoMapper;

    @BeforeEach
    void setUp() {
        todoRepository = mock(TodoRepository.class);
        todoMapper = mock(TodoMapper.class);
        
        todoQueryService = new TodoQueryServiceImpl(todoRepository, todoMapper);
    }

    @Test
    @DisplayName("PUBLIC Todo 조회 성공")
    void getTodoById_Public_Success() throws Exception {
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
                .content("테스트 내용")
                .visibility(Visibility.PUBLIC)
                .priority(BigDecimal.ONE)
                .build();

        ResponseTodoDto responseDto = new ResponseTodoDto(
                todoId, ownerId, "owner", "테스트 제목", "테스트 내용",
                false, LocalDateTime.now(), null, Visibility.PUBLIC,
                BigDecimal.ONE, List.of(), LocalDateTime.now(), LocalDateTime.now()
        );

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));
        given(todoMapper.toResponseDto(any(Todo.class))).willReturn(responseDto);

        // when
        ResponseTodoDto result = todoQueryService.getTodoById(todoId, requestMemberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(todoId);
        verify(todoRepository).findByIdAndNotDeleted(todoId);
    }

    private void setMemberId(Member member, Long id) throws Exception {
        java.lang.reflect.Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, id);
    }

    @Test
    @DisplayName("PRIVATE Todo를 소유자가 아닌 사람이 조회 시 예외 발생")
    void getTodoById_PrivateWithoutPermission() throws Exception {
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
                .visibility(Visibility.PRIVATE)
                .build();

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));

        // when & then
        assertThatThrownBy(() -> todoQueryService.getTodoById(todoId, requestMemberId))
                .isInstanceOf(CustomException.class)
                .hasMessage("조회 권한이 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 Todo 조회 시 예외 발생")
    void getTodoById_NotFound() {
        // given
        Long todoId = 999L;
        Long requestMemberId = 1L;

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> todoQueryService.getTodoById(todoId, requestMemberId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Todo를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("Todo 목록 조회 성공")
    void getAllTodos_Success() throws Exception {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        
        RequestTodoFilterDto filterDto = new RequestTodoFilterDto(
                memberId, null, false, "priority", "asc"
        );

        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);
        
        List<Todo> todoList = new ArrayList<>();
        todoList.add(Todo.builder()
                .id(1L)
                .member(member)
                .title("제목1")
                .visibility(Visibility.PUBLIC)
                .build());

        Page<Todo> todoPage = new PageImpl<>(todoList, pageable, 1);

        ResponseTodoDto responseDto = new ResponseTodoDto(
                1L, memberId, "user1", "제목1", "내용1",
                false, LocalDateTime.now(), null, Visibility.PUBLIC,
                BigDecimal.ONE, List.of(), LocalDateTime.now(), LocalDateTime.now()
        );

        given(todoRepository.findByFiltersWithPagination(
                any(), any(), any(), any(Pageable.class)
        )).willReturn(todoPage);
        given(todoMapper.toResponseDto(any(Todo.class))).willReturn(responseDto);

        // when
        PageDto<ResponseTodoDto> result = todoQueryService.getAllTodos(filterDto, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        verify(todoRepository).findByFiltersWithPagination(any(), any(), any(), any(Pageable.class));
    }
}

