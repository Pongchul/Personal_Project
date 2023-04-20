package hello.project.article.domain;

import hello.project.comm.BaseDateTime;
import hello.project.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String contents;

    private String destination;

    private String location;

    @Enumerated(EnumType.STRING)
    private CurrentState currentState;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    public Article(Long id, String title, String contents, String destination, String location, CurrentState currentState, Member author,
                   LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.destination = destination;
        this.location = location;
        this.currentState = currentState;
        this.author = author;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }
}
