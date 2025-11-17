package com.pwc.todoworksapce.todo.service.impl;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.entity.enums.Department;
import com.pwc.todoworksapce.member.repository.MemberRepository;
import com.pwc.todoworksapce.tag.entity.Tag;
import com.pwc.todoworksapce.tag.repository.TagRepository;
import com.pwc.todoworksapce.todo.controller.dto.RequestCreateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseCreateTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.TodoTag;
import com.pwc.todoworksapce.todo.entity.enums.Visibility;
import com.pwc.todoworksapce.todo.mapper.TodoMapper;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import com.pwc.todoworksapce.todo.repository.TodoTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TodoCreateServiceImplTest {

    private TodoCreateServiceImpl todoCreateService;
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
        
        todoCreateService = new TodoCreateServiceImpl(
                todoRepository,
                tagRepository,
                todoTagRepository,
                memberRepository,
                todoMapper
        );
    }

    @Test
    @DisplayName("Todo 생성 성공")
    void createTodo_Success() throws Exception {
        // given
        Long memberId = 1L;
        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);

        RequestCreateTodoDto requestDto = new RequestCreateTodoDto(
                memberId,
                "테스트 제목",
                "테스트 내용",
                LocalDateTime.now().plusDays(7),
                List.of("개발", "테스트"),
                Visibility.PUBLIC,
                BigDecimal.ONE
        );

        Todo savedTodo = Todo.builder()
                .id(1L)
                .member(member)
                .title(requestDto.title())
                .content(requestDto.content())
                .dueDate(requestDto.dueDate())
                .visibility(requestDto.visibility())
                .priority(requestDto.priority())
                .todoTags(new ArrayList<>())
                .build();

        Tag tag1 = new Tag(member, "개발");
        Tag tag2 = new Tag(member, "테스트");

        ResponseCreateTodoDto responseDto = new ResponseCreateTodoDto(
                1L, memberId, "nickname", "테스트 제목", "테스트 내용",
                false, LocalDateTime.now().plusDays(7), 7,
                Visibility.PUBLIC, BigDecimal.ONE, List.of("개발", "테스트"),
                LocalDateTime.now(), LocalDateTime.now()
        );

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(todoRepository.save(any(Todo.class))).willReturn(savedTodo);
        given(tagRepository.findByNameAndMemberId("개발", memberId)).willReturn(Optional.of(tag1));
        given(tagRepository.findByNameAndMemberId("테스트", memberId)).willReturn(Optional.of(tag2));
        given(todoMapper.toResponseCreateDto(any(Todo.class), any(List.class))).willReturn(responseDto);

        // when
        ResponseCreateTodoDto result = todoCreateService.createTodo(requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.tagNames()).containsExactly("개발", "테스트");
        verify(todoRepository).save(any(Todo.class));
    }

    private void setMemberId(Member member, Long id) throws Exception {
        java.lang.reflect.Field field = Member.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(member, id);
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 Todo 생성 시 예외 발생")
    void createTodo_MemberNotFound() {
        // given
        RequestCreateTodoDto requestDto = new RequestCreateTodoDto(
                999L,
                "테스트 제목",
                "테스트 내용",
                LocalDateTime.now().plusDays(7),
                List.of("개발"),
                Visibility.PUBLIC,
                BigDecimal.ONE
        );

        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> todoCreateService.createTodo(requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("우선순위가 1 미만일 때 예외 발생")
    void createTodo_InvalidPriority() throws Exception {
        // given
        Long memberId = 1L;
        Member member = new Member("user1", "password", "nickname", Department.IT);
        setMemberId(member, memberId);

        RequestCreateTodoDto requestDto = new RequestCreateTodoDto(
                memberId,
                "테스트 제목",
                "테스트 내용",
                LocalDateTime.now().plusDays(7),
                List.of(),
                Visibility.PUBLIC,
                BigDecimal.ZERO  // 잘못된 우선순위
        );

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> todoCreateService.createTodo(requestDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("우선순위는 1 이상이어야 합니다.");
    }
}

