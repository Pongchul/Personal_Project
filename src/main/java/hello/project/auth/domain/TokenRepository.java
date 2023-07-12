package hello.project.auth.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends Repository<Token, Long> {

    Token save(Token token);

    Optional<Token> findByMemberId(Long memberId);

    @Modifying
    @Query("delete from Token t where t.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
