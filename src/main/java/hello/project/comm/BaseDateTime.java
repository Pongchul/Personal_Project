package hello.project.comm;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)              // JPA Entity 에서 이벤트가 발생할 때마다 특정 로직을 실행시킨다.
@MappedSuperclass
@Getter
public abstract class BaseDateTime {

    @CreatedDate
    protected LocalDateTime createdTime;

    @LastModifiedDate
    protected LocalDateTime modifiedTime;
}
