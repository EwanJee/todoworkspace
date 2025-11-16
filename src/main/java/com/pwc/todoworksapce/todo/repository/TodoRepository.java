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

    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN FETCH t.member
            LEFT JOIN FETCH t.todoTags tt
            LEFT JOIN FETCH tt.tag
            WHERE t.id = :id AND t.deletedAt IS NULL
            """)
    Optional<Todo> findByIdAndNotDeleted(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Todo t SET t.deletedAt = CURRENT_TIMESTAMP WHERE t.id = :id AND t.deletedAt IS NULL")
    int softDeleteById(@Param("id") Long id);


    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN FETCH t.member
            LEFT JOIN t.todoTags tt
            LEFT JOIN tt.tag tag
            WHERE (:memberId IS NULL OR t.member.id = :memberId)
            AND (:tagName IS NULL OR tag.name = :tagName)
            AND (:isCompleted IS NULL OR t.isCompleted = :isCompleted)
            AND t.deletedAt IS NULL
            """)
    Page<Todo> findByFiltersWithPagination(
            @Param("memberId") Long memberId,
            @Param("tagName") String tagName,
            @Param("isCompleted") Boolean isCompleted,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t FROM Todo t
            LEFT JOIN FETCH t.todoTags tt
            LEFT JOIN FETCH tt.tag
            WHERE t IN :todos
            """)
    List<Todo> findTodosWithTags(@Param("todos") List<Todo> todos);
}
