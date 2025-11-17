package com.pwc.todoworksapce.todo.service.impl;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.entity.enums.Department;
import com.pwc.todoworksapce.member.repository.MemberRepository;
import com.pwc.todoworksapce.tag.entity.Tag;
import com.pwc.todoworksapce.tag.repository.TagRepository;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdatePriorityDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.enums.Visibility;
import com.pwc.todoworksapce.todo.mapper.TodoMapper;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import com.pwc.todoworksapce.todo.repository.TodoTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TodoUpdateServiceImplTest {

    private TodoUpdateServiceImpl todoUpdateService;
    private TodoRepository todoRepository;
    private TagRepository tagRepository;
    private TodoTagRepository todoTagRepository;
    private MemberRepository memberRepository;
    private TodoMapper todoMapper;

    @BeforeEach
    void setUp() {
        todoRepository = mock(TodoRepository.class);
        tagRepository = mock(TagRepository.class);
        todoTagRepository = mock(TodoTagRepository.class);
        memberRepository = mock(MemberRepository.class);
        todoMapper = mock(TodoMapper.class);
        
        todoUpdateService = new TodoUpdateServiceImpl(
                todoRepository,
                tagRepository,
                todoTagRepository,
                memberRepository,
                todoMapper
        );
    }

    private void setMemberId(Member member, Long id) throws Exception {
        java.lang.reflect.Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, id);
    }

    @Test
    @DisplayName("Todo 수정 성공")
    void updateTodo_Success() throws Exception {
        // given
        Long todoId = 1L;
        Long memberId = 1L;
        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);

        Todo todo = Todo.builder()
                .id(todoId)
                .member(member)
                .title("원본 제목")
                .content("원본 내용")
                .visibility(Visibility.PUBLIC)
                .priority(BigDecimal.ONE)
                .build();

        RequestUpdateTodoDto requestDto = new RequestUpdateTodoDto(
                memberId,
                "수정된 제목",
                "수정된 내용",
                true,
                LocalDateTime.now().plusDays(7),
                List.of("수정태그"),
                Visibility.PRIVATE,
                BigDecimal.valueOf(2)
        );

        ResponseTodoDto responseDto = new ResponseTodoDto(
                todoId, memberId, "user1", "수정된 제목", "수정된 내용",
                true, LocalDateTime.now().plusDays(7), 7, Visibility.PRIVATE,
                BigDecimal.valueOf(2), List.of("수정태그"), LocalDateTime.now(), LocalDateTime.now()
        );

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(tagRepository.findByNameAndMemberId(any(), any())).willReturn(Optional.of(new Tag(member, "수정태그")));
        given(todoMapper.toResponseDto(any(Todo.class))).willReturn(responseDto);

        // when
        ResponseTodoDto result = todoUpdateService.updateTodo(todoId, requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("수정된 제목");
        verify(todoRepository).findByIdAndNotDeleted(todoId);
        verify(todoTagRepository).deleteByTodoId(todoId);
    }

    @Test
    @DisplayName("소유자가 아닌 사람이 Todo 수정 시 예외 발생")
    void updateTodo_WithoutPermission() throws Exception {
        // given
        Long todoId = 1L;
        Long ownerId = 1L;
        Long requestMemberId = 2L;
        Member owner = new Member("owner", "password", "nickname", Department.IT);
        setMemberId(owner, ownerId);

        Todo todo = Todo.builder()
                .id(todoId)
                .member(owner)
                .title("원본 제목")
                .build();

        RequestUpdateTodoDto requestDto = new RequestUpdateTodoDto(
                requestMemberId,
                "수정된 제목",
                "수정된 내용",
                false,
                null,
                List.of(),
                Visibility.PUBLIC,
                BigDecimal.ONE
        );

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));

        // when & then
        assertThatThrownBy(() -> todoUpdateService.updateTodo(todoId, requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("수정 권한이 없습니다.");
    }

    @Test
    @DisplayName("우선순위 변경 성공")
    void updateTodoPriority_Success() throws Exception {
        // given
        Long todoId = 1L;
        Long memberId = 1L;
        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);

        Todo todo = Todo.builder()
                .id(todoId)
                .member(member)
                .title("제목")
                .priority(BigDecimal.ONE)
                .build();

        RequestUpdatePriorityDto requestDto = new RequestUpdatePriorityDto(
                memberId,
                BigDecimal.valueOf(5)
        );

        ResponseTodoDto responseDto = new ResponseTodoDto(
                todoId, memberId, "user1", "제목", "내용",
                false, LocalDateTime.now(), null, Visibility.PUBLIC,
                BigDecimal.valueOf(5), List.of(), LocalDateTime.now(), LocalDateTime.now()
        );

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));
        given(todoMapper.toResponseDto(any(Todo.class))).willReturn(responseDto);

        // when
        ResponseTodoDto result = todoUpdateService.updateTodoPriority(todoId, requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.priority()).isEqualTo(BigDecimal.valueOf(5));
        verify(todoRepository).findByIdAndNotDeleted(todoId);
    }

    @Test
    @DisplayName("우선순위가 1 미만일 때 예외 발생")
    void updateTodoPriority_InvalidPriority() throws Exception {
        // given
        Long todoId = 1L;
        Long memberId = 1L;
        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);

        Todo todo = Todo.builder()
                .id(todoId)
                .member(member)
                .title("제목")
                .priority(BigDecimal.ONE)
                .build();

        RequestUpdatePriorityDto requestDto = new RequestUpdatePriorityDto(
                memberId,
                BigDecimal.ZERO  // 잘못된 우선순위
        );

        given(todoRepository.findByIdAndNotDeleted(todoId)).willReturn(Optional.of(todo));

        // when & then
        assertThatThrownBy(() -> todoUpdateService.updateTodoPriority(todoId, requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("우선순위는 1 이상이어야 합니다.");
    }
}

