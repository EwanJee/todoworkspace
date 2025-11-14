package com.pwc.todoworksapce.todo.entity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pwc.todoworksapce.base.BaseEntity;
import com.pwc.todoworksapce.member.entity.Member;
import com.pwc.todoworksapce.todo.controller.dto.RequestUpdateTodoDto;
import com.pwc.todoworksapce.todo.entity.enums.Visibility;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Todo 엔티티 - 사용자가 작성한 할 일
 */
@Entity
@Table(name = "todos")
@Getter
@Builder
@AllArgsConstructor
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isCompleted = false;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.PUBLIC;

    @Column(nullable = false)
    private BigDecimal priority = BigDecimal.ONE;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoTag> todoTags = new ArrayList<>();

    protected Todo() {
    }

    /**
     * D-Day
     *
     * @return 남은 일수 (음수는 마감일이 지난 경우)
     */
    public int getRemainingDays() {
        LocalDateTime now = LocalDateTime.now();
        return (int) Duration.between(now, dueDate).toDays();
    }

    /**
     * 우선순위 수정
     */
    public void updatePriority(BigDecimal priority) {
        this.priority = priority;
    }

    /**
     * DTO를 통한 Todo 필드 일괄 업데이트 (null이 아닌 필드만)
     *
     * @param dto 업데이트할 내용이 담긴 DTO
     */
    public void update(RequestUpdateTodoDto dto) {
        if (dto.title() != null) {
            this.title = dto.title();
        }
        if (dto.content() != null) {
            this.content = dto.content();
        }
        if (dto.dueDate() != null) {
            this.dueDate = dto.dueDate();
        }
        if (dto.visibility() != null) {
            this.visibility = dto.visibility();
        }
        if (dto.priority() != null) {
            this.priority = dto.priority();
        }
        if (dto.isCompleted() != null) {
            this.isCompleted = dto.isCompleted();
        }
    }

    /**
     * TodoTag 추가
     */
    public void addTodoTag(TodoTag todoTag) {
        this.todoTags.add(todoTag);
    }

    /**
     * 특정 회원이 이 Todo의 소유자인지 확인
     *
     * @param memberId 확인할 회원 ID
     * @return 소유자이면 true
     */
    public boolean isOwnedBy(Long memberId) {
        return this.member.getId().equals(memberId);
    }
}
