package com.pwc.todoworksapce.tag.repository;

import com.pwc.todoworksapce.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.name = :name AND t.member.id = :memberId AND t.deletedAt IS NULL")
    Optional<Tag> findByNameAndMemberId(@Param("name") String name, @Param("memberId") Long memberId);

    @Query("SELECT t FROM Tag t WHERE t.name = :name AND t.deletedAt IS NULL")
    Optional<Tag> findByName(@Param("name") String name);
}
