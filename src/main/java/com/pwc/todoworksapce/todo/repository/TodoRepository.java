package com.pwc.todoworksapce.todo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pwc.todoworksapce.todo.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Todo> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT t FROM Todo t WHERE t.deletedAt IS NULL ORDER BY t.priority ASC, t.dueDate ASC")
    List<Todo> findAllNotDeleted();

    @Query("SELECT t FROM Todo t WHERE t.member.id = :memberId AND t.deletedAt IS NULL ORDER BY t.priority ASC, t.dueDate ASC")
    List<Todo> findByMemberIdAndNotDeleted(@Param("memberId") Long memberId);

    @Query("SELECT t FROM Todo t WHERE t.isCompleted = :isCompleted AND t.deletedAt IS NULL ORDER BY t.priority ASC, t.dueDate ASC")
    List<Todo> findByIsCompletedAndNotDeleted(@Param("isCompleted") boolean isCompleted);

    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN t.todoTags tt
            LEFT JOIN tt.tag tag
            WHERE (:memberId IS NULL OR t.member.id = :memberId)
            AND (:tagName IS NULL OR tag.name = :tagName)
            AND (:isCompleted IS NULL OR t.isCompleted = :isCompleted)
            AND t.dueDate > :now
            AND t.deletedAt IS NULL
            ORDER BY t.priority ASC, t.dueDate ASC
            """)
    List<Todo> findByFilters(
            @Param("memberId") Long memberId,
            @Param("tagName") String tagName,
            @Param("isCompleted") Boolean isCompleted,
            @Param("now") LocalDateTime now
    );

    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN t.todoTags tt
            LEFT JOIN tt.tag tag
            WHERE (:memberId IS NULL OR t.member.id = :memberId)
            AND (:tagName IS NULL OR tag.name = :tagName)
            AND (:isCompleted IS NULL OR t.isCompleted = :isCompleted)
            AND t.dueDate > :now
            AND t.deletedAt IS NULL
            ORDER BY t.dueDate ASC
            """)
    List<Todo> findByFiltersOrderByDueDate(
            @Param("memberId") Long memberId,
            @Param("tagName") String tagName,
            @Param("isCompleted") Boolean isCompleted,
            @Param("now") LocalDateTime now
    );

    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN t.todoTags tt
            LEFT JOIN tt.tag tag
            WHERE (:memberId IS NULL OR t.member.id = :memberId)
            AND (:tagName IS NULL OR tag.name = :tagName)
            AND (:isCompleted IS NULL OR t.isCompleted = :isCompleted)
            AND t.dueDate > :now
            AND t.deletedAt IS NULL
            ORDER BY t.dueDate DESC
            """)
    List<Todo> findByFiltersOrderByDueDateDesc(
            @Param("memberId") Long memberId,
            @Param("tagName") String tagName,
            @Param("isCompleted") Boolean isCompleted,
            @Param("now") LocalDateTime now
    );

    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN t.todoTags tt
            LEFT JOIN tt.tag tag
            WHERE (:memberId IS NULL OR t.member.id = :memberId)
            AND (:tagName IS NULL OR tag.name = :tagName)
            AND (:isCompleted IS NULL OR t.isCompleted = :isCompleted)
            AND t.dueDate > :now
            AND t.deletedAt IS NULL
            ORDER BY t.createdAt DESC
            """)
    List<Todo> findByFiltersOrderByCreatedAtDesc(
            @Param("memberId") Long memberId,
            @Param("tagName") String tagName,
            @Param("isCompleted") Boolean isCompleted,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("UPDATE Todo t SET t.deletedAt = CURRENT_TIMESTAMP WHERE t.id = :id AND t.deletedAt IS NULL")
    int softDeleteById(@Param("id") Long id);

    @Query("""
            SELECT t FROM Todo t
            WHERE (t.visibility = 'PUBLIC' OR t.member.id = :memberId)
            AND t.deletedAt IS NULL
            ORDER BY t.priority ASC, t.dueDate ASC
            """)
    List<Todo> findAccessibleTodosByMemberId(@Param("memberId") Long memberId);

    // Pagination versions
    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN t.todoTags tt
            LEFT JOIN tt.tag tag
            WHERE (:memberId IS NULL OR t.member.id = :memberId)
            AND (:tagName IS NULL OR tag.name = :tagName)
            AND (:isCompleted IS NULL OR t.isCompleted = :isCompleted)
            AND t.dueDate > :now
            AND t.deletedAt IS NULL
            """)
    Page<Todo> findByFiltersWithPagination(
            @Param("memberId") Long memberId,
            @Param("tagName") String tagName,
            @Param("isCompleted") Boolean isCompleted,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );
}
