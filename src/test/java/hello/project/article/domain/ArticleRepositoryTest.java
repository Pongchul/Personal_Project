package hello.project.article.domain;

import hello.project.article.domain.search.ArticleSearchRepository;
import hello.project.auth.support.SHA256Encoder;
import hello.project.member.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest(includeFilters = @ComponentScan.Filter(classes = Repositories.class))
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleSearchRepository articleSearchRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    private Password password;
    private Member host;

    @BeforeEach
    void setUp() {
        password = Password.encrypt("pongchul1!", new SHA256Encoder());
        host = memberRepository.save(new Member(UserId.userId("모임주최"), password, UserName.from("pongchul")));
    }

//    @DisplayName("스케쥴이 지정된 모임을 저장한다")
//    @Test
//    void saveArticleWith

}