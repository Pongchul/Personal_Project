package hello.project.article.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
@ToString(includeFieldNames = false)
@AllArgsConstructor
public class Location {

    @Column(name = "locationAddress", nullable = false)
    private String address;

    @Column(name = "locationDetail", nullable = false)
    private String detail;
}
