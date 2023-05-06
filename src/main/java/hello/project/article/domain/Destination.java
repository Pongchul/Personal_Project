package hello.project.article.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
@ToString(includeFieldNames = false)
@AllArgsConstructor
public class Destination {

    @Column(name = "destinationAddress", nullable = false)
    private String address;

    @Column(name = "destinationDetail", nullable = false)
    private String detail;
}
