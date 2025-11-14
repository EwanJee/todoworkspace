package com.pwc.todoworksapce.todo.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.todoworksapce.todo.controller.dto.PageDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestCreateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestTodoFilterDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdatePriorityDto;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseCreateTodoDto;
import com.pwc.todoworksapce.todo.controller.dto.ResponseTodoDto;
import com.pwc.todoworksapce.todo.service.TodoCreateService;
import com.pwc.todoworksapce.todo.service.TodoDeleteService;
import com.pwc.todoworksapce.todo.service.TodoQueryService;
import com.pwc.todoworksapce.todo.service.TodoUpdateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Todo 관련 REST API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "투두", description = "투두 API")
@RequiredArgsConstructor
public class TodoController {

    private final TodoQueryService todoQueryService;
    private final TodoCreateService todoCreateService;
    private final TodoUpdateService todoUpdateService;
    private final TodoDeleteService todoDeleteService;

    /**
     * 새로운 Todo 생성
     *
     * @param requestCreateTodoDto Todo 생성 요청 정보
     * @return 생성된 Todo 정보
     */
    @PostMapping("")
    @Operation(summary = "투두 생성", description = "새로운 투두 생성")
    public ResponseEntity<ResponseCreateTodoDto> createTodo(
            @Valid @RequestBody RequestCreateTodoDto requestCreateTodoDto) {
        ResponseCreateTodoDto responseCreateTodoDto = todoCreateService.createTodo(requestCreateTodoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseCreateTodoDto);
    }

    /**
     * ID로 특정 Todo 조회 (공개된 Todo이거나 본인이 작성한 Todo만 조회 가능)
     *
     * @param id       Todo ID
     * @param memberId 요청한 회원 ID
     * @return Todo 정보
     */
    @GetMapping("/{id}")
    @Operation(summary = "투두 단건 조회", description = "ID로 특정 투두 조회 (공개된 투두이거나 본인이 작성한 투두만 조회 가능)")
    public ResponseEntity<ResponseTodoDto> getTodoById(
            @Parameter(description = "조회할 Todo ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "요청 회원 ID", required = true)
            @RequestParam Long memberId) {
        ResponseTodoDto responseTodoDto = todoQueryService.getTodoById(id, memberId);
        return ResponseEntity.ok(responseTodoDto);
    }

    /**
     * 필터링과 정렬을 적용한 Todo 목록 조회 (페이지네이션)
     *
     * @param memberId    회원 ID (선택)
     * @param tagName     태그 이름 (선택)
     * @param isCompleted 완료 여부 (선택)
     * @param sortBy      정렬 기준 (priority, dueDate, createdAt)
     * @param sortOrder   정렬 순서 (asc, desc)
     * @param page        페이지 번호 (0부터 시작, 기본값: 0)
     * @param size        페이지 크기 (기본값: 10)
     * @return Todo 목록 (페이지네이션)
     */
    @GetMapping("")
    @Operation(summary = "투두 목록 조회", description = "필터링과 정렬을 적용한 투두 목록 조회 (페이지네이션)")
    public ResponseEntity<PageDto<ResponseTodoDto>> getAllTodos(
            @Parameter(description = "회원 ID (필터링)") @RequestParam(required = false) Long memberId,
            @Parameter(description = "태그 이름 (필터링)") @RequestParam(required = false) String tagName,
            @Parameter(description = "완료 여부 (필터링)") @RequestParam(required = false) Boolean isCompleted,
            @Parameter(description = "정렬 기준 (priority, dueDate, createdAt)") @RequestParam(required = false, defaultValue = "priority") String sortBy,
            @Parameter(description = "정렬 순서 (asc, desc)") @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(required = false, defaultValue = "10") int size) {

        RequestTodoFilterDto filterDto = new RequestTodoFilterDto(
                memberId, tagName, isCompleted, sortBy, sortOrder
        );

        // 정렬 기준 설정
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageDto<ResponseTodoDto> todos = todoQueryService.getAllTodos(filterDto, pageable);
        return ResponseEntity.ok(todos);
    }

    /**
     * Todo 수정 (작성자만 수정 가능)
     *
     * @param id                   Todo ID
     * @param requestUpdateTodoDto Todo 수정 요청 정보
     * @return 수정된 Todo 정보
     */
    @PutMapping("/{id}")
    @Operation(summary = "투두 수정", description = "투두 수정 (작성자만 수정 가능)")
    public ResponseEntity<ResponseTodoDto> updateTodo(
            @Parameter(description = "수정할 Todo ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RequestUpdateTodoDto requestUpdateTodoDto) {
        ResponseTodoDto responseTodoDto = todoUpdateService.updateTodo(id, requestUpdateTodoDto);
        return ResponseEntity.ok(responseTodoDto);
    }

    /**
     * Todo 삭제 (Soft Delete, 작성자만 삭제 가능)
     *
     * @param id       Todo ID
     * @param memberId 요청한 회원 ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "투두 삭제", description = "투두 삭제 (Soft Delete, 작성자만 삭제 가능)")
    public ResponseEntity<Void> deleteTodo(
            @Parameter(description = "삭제할 Todo ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "요청 회원 ID", required = true)
            @RequestParam Long memberId) {
        todoDeleteService.deleteTodo(id, memberId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Todo 우선순위 변경을 통한 순서 조정 (작성자만 변경 가능)
     *
     * @param id                       Todo ID
     * @param requestUpdatePriorityDto 우선순위 변경 요청 정보
     * @return 수정된 Todo 정보
     */
    @PatchMapping("/{id}/priority")
    @Operation(summary = "투두 순서 변경", description = "투두 우선순위 변경을 통한 순서 조정 (작성자만 변경 가능)")
    public ResponseEntity<ResponseTodoDto> updateTodoPriority(
            @Parameter(description = "수정할 Todo ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RequestUpdatePriorityDto requestUpdatePriorityDto) {
        ResponseTodoDto responseTodoDto = todoUpdateService.updateTodoPriority(id, requestUpdatePriorityDto);
        return ResponseEntity.ok(responseTodoDto);
    }
}
