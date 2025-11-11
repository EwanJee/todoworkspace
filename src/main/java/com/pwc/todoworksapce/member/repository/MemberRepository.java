package com.pwc.todoworksapce.member.repository;

import com.pwc.todoworksapce.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member as m WHERE m.username = :username AND m.deletedAt IS NULL")
    boolean existsByUsername(String username);

    @Modifying
    @Query("""
                UPDATE Member m
                SET m.deletedAt = NOW()
                WHERE m.id = :memberId
                  AND m.role = 'USER'
                  AND m.deletedAt IS NULL
            """)
    int softDeleteMemberById(Long memberId);

    default boolean deleteMemberById(Long memberId) {
        return softDeleteMemberById(memberId) > 0;
    }
}
