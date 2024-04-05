package Springboot.JWT.Repository;

import Springboot.JWT.Model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            Select t from Token t inner join Member m
            on t.member.id = m.id
            where t.member.id = :memberId and t.loggedOut = false
    """)
    List<Token> findAllTokensByMember(Integer memberId);

    Optional<Token> findByToken(String token);
}
