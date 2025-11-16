package com.pwc.todoworksapce.todo.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pwc.todoworksapce.todo.controller.dto.ResponseCreateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;
import com.pwc.todoworksapce.todo.entity.Todo;

/**
 * Todo Entity와 DTO 간의 변환을 담당하는 Mapper 클래스
 */
@Component
public class TodoMapper {

    /**
     * Todo Entity를 ResponseTodoDto로 변환
     *
     * @param todo 변환할 Todo Entity
     * @return 변환된 ResponseTodoDto
     */
    public ResponseTodoDto toResponseDto(Todo todo) {
        if (todo == null) {
            return null;
        }

        List<String> tagNames = extractTagNames(todo);

        return new ResponseTodoDto(
                todo.getId(),
                todo.getMember().getId(),
                todo.getMember().getNickname(),
                todo.getTitle(),
                todo.getContent(),
                todo.isCompleted(),
                todo.getDueDate(),
                todo.getRemainingDays(),
                todo.getVisibility(),
                todo.getPriority(),
                tagNames,
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    /**
     * Todo Entity를 ResponseCreateTodoDto로 변환
     *
     * @param todo 변환할 Todo Entity
     * @param tagNames 태그 이름 목록
     * @return 변환된 ResponseCreateTodoDto
     */
    public ResponseCreateTodoDto toResponseCreateDto(Todo todo, List<String> tagNames) {
        if (todo == null) {
            return null;
        }

        return new ResponseCreateTodoDto(
                todo.getId(),
                todo.getMember().getId(),
                todo.getMember().getNickname(),
                todo.getTitle(),
                todo.getContent(),
                todo.isCompleted(),
                todo.getDueDate(),
                todo.getRemainingDays(),
                todo.getVisibility(),
                todo.getPriority(),
                tagNames != null ? tagNames : Collections.emptyList(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }

    /**
     * Todo Entity 리스트를 ResponseTodoDto 리스트로 변환
     *
     * @param todos 변환할 Todo Entity 리스트
     * @return 변환된 ResponseTodoDto 리스트
     */
    public List<ResponseTodoDto> toResponseDtoList(List<Todo> todos) {
        if (todos == null || todos.isEmpty()) {
            return Collections.emptyList();
        }

        return todos.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Todo에서 태그 이름 목록 추출
     *
     * @param todo Todo Entity
     * @return 태그 이름 목록
     */
    private List<String> extractTagNames(Todo todo) {
        if (todo.getTodoTags() == null || todo.getTodoTags().isEmpty()) {
            return Collections.emptyList();
        }

        return todo.getTodoTags().stream()
                .map(todoTag -> todoTag.getTag().getName())
                .collect(Collectors.toList());
    }
}

