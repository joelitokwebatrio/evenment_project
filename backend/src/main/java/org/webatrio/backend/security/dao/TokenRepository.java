package org.webatrio.backend.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.webatrio.backend.security.models.token.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
            select t from Token t inner join Participant u\s
            on t.participant.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);
}
