package com.pwc.todoworksapce.todo.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pwc.todoworksapce.error.CustomException;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.member.repository.MemberRepository;
import com.pwc.todoworksapce.tag.entity.Tag;
import com.pwc.todoworksapce.tag.repository.TagRepository;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdatePriorityDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;
import com.pwc.todoworksapce.todo.entity.TodoTag;
import com.pwc.todoworksapce.todo.mapper.TodoMapper;
import com.pwc.todoworksapce.todo.repository.TodoRepository;
import com.pwc.todoworksapce.todo.repository.TodoTagRepository;
import com.pwc.todoworksapce.todo.service.TodoUpdateService;

import lombok.RequiredArgsConstructor;

/**
 * Todo 수정 서비스 구현체
 */
@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class TodoUpdateServiceImpl implements TodoUpdateService {

    private final TodoRepository todoRepository;
    private final TagRepository tagRepository;
    private final TodoTagRepository todoTagRepository;
    private final MemberRepository memberRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional
    public ResponseTodoDto updateTodo(Long id, RequestUpdateTodoDto requestUpdateTodoDto) {
        Todo todo = findTodoById(id);
        validateUpdatePermission(todo, requestUpdateTodoDto.memberId());

        updateTodoFields(todo, requestUpdateTodoDto);
        updateTodoTags(todo, requestUpdateTodoDto.tagNames(), requestUpdateTodoDto.memberId());

        return todoMapper.toResponseDto(todo);
    }

    @Override
    @Transactional
    public ResponseTodoDto updateTodoPriority(Long id, RequestUpdatePriorityDto requestUpdatePriorityDto) {
        Todo todo = findTodoById(id);
        validateUpdatePermission(todo, requestUpdatePriorityDto.memberId());
        validatePriority(requestUpdatePriorityDto.priority());

        todo.updatePriority(requestUpdatePriorityDto.priority());

        return todoMapper.toResponseDto(todo);
    }

    private Todo findTodoById(Long id) {
        return todoRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CustomException("Todo를 찾을 수 없습니다."));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException("회원이 존재하지 않습니다."));
    }

    private void updateTodoFields(Todo todo, RequestUpdateTodoDto dto) {
        // 우선순위 먼저 검증
        if (dto.priority() != null) {
            validatePriority(dto.priority());
        }
        todo.update(dto);
    }

    private void updateTodoTags(Todo todo, java.util.List<String> tagNames, Long memberId) {
        if (tagNames == null) {
            return;
        }

        todoTagRepository.deleteByTodoId(todo.getId());

        Member member = findMemberById(memberId);
        for (String tagName : tagNames) {
            Tag tag = findOrCreateTag(tagName, member);
            createTodoTag(todo, tag);
        }
    }

    private Tag findOrCreateTag(String tagName, Member member) {
        return tagRepository.findByNameAndMemberId(tagName, member.getId())
                .orElseGet(() -> tagRepository.save(new Tag(member, tagName)));
    }

    private void createTodoTag(Todo todo, Tag tag) {
        TodoTag todoTag = new TodoTag(todo, tag);
        todoTagRepository.save(todoTag);
    }

    private void validateUpdatePermission(Todo todo, Long requestMemberId) {
        if (!todo.isOwnedBy(requestMemberId)) {
            throw new CustomException("수정 권한이 없습니다.");
        }
    }

    private void validatePriority(BigDecimal priority) {
        if (priority != null && priority.compareTo(BigDecimal.ONE) < 0) {
            throw new CustomException("우선순위는 1 이상이어야 합니다.");
        }
    }
}
