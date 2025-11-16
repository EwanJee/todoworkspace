package com.pwc.todoworksapce.todo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.repository.MemberRepository;
import com.pwc.todoworksapce.tag.entity.Tag;
import com.pwc.todoworksapce.tag.repository.TagRepository;
import com.pwc.todoworksapce.todo.controller.dto.RequestCreateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseCreateTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.TodoTag;
import com.pwc.todoworksapce.todo.mapper.TodoMapper;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import com.pwc.todoworksapce.todo.repository.TodoTagRepository;
import com.pwc.todoworksapce.todo.service.TodoCreateService;

import lombok.RequiredArgsConstructor;

/**
 * Todo 생성 서비스 구현체
 */
@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class TodoCreateServiceImpl implements TodoCreateService {

    private final TodoRepository todoRepository;
    private final TagRepository tagRepository;
    private final TodoTagRepository todoTagRepository;
    private final MemberRepository memberRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional
    public ResponseCreateTodoDto createTodo(RequestCreateTodoDto requestCreateTodoDto) {
        Member member = findMemberById(requestCreateTodoDto.memberId());
        validatePriority(requestCreateTodoDto.priority());

        Todo todo = buildTodo(requestCreateTodoDto, member);
        Todo savedTodo = todoRepository.save(todo);

        List<String> tagNames = processTags(savedTodo, requestCreateTodoDto.tagNames(), member);

        return todoMapper.toResponseCreateDto(savedTodo, tagNames);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("회원이 존재하지 않습니다."));
    }

    private Todo buildTodo(RequestCreateTodoDto dto, Member member) {
        return Todo.builder()
                .member(member)
                .title(dto.title())
                .content(dto.content())
                .dueDate(dto.dueDate())
                .visibility(dto.visibility())
                .priority(dto.priority())
                .build();
    }

    private List<String> processTags(Todo todo, List<String> tagNames, Member member) {
        List<String> processedTagNames = new ArrayList<>();

        if (tagNames == null || tagNames.isEmpty()) {
            return processedTagNames;
        }

        for (String tagName : tagNames) {
            Tag tag = findOrCreateTag(tagName, member);
            createTodoTag(todo, tag);
            processedTagNames.add(tag.getName());
        }

        return processedTagNames;
    }

    private Tag findOrCreateTag(String tagName, Member member) {
        return tagRepository.findByNameAndMemberId(tagName, member.getId())
                .orElseGet(() -> tagRepository.save(new Tag(member, tagName)));
    }

    private void createTodoTag(Todo todo, Tag tag) {
        TodoTag todoTag = new TodoTag(todo, tag);
        todoTagRepository.save(todoTag);
        todo.addTodoTag(todoTag);
    }

    private void validatePriority(BigDecimal priority) {
        if (priority != null && priority.compareTo(BigDecimal.ONE) < 0) {
            throw new CustomException("우선순위는 1 이상이어야 합니다.");
        }
    }
}

