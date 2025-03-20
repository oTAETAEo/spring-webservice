package com.jojoldu.book.springboot.domain.posts;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * '@MappedSuperclass'                                  JPA Entity 클래스들이 BaseTimeEntity 를 상속할 경우 BaseTimeEntity 의 필드도 컬럼으로 인식한다.
 * '@EntityListeners(AuditingEntityListener.class)'     BaseTimeEntity 에 Auditing 기능을 포함 시킨다.
 * '@CreatedDate'                                       Entity 가 생성되어 저장될때 시간을 자동 저장.
 * '@LastModifiedDate'                                  조회한 Entity 의 값을 변경할때 시간을 자동 저장.
 */

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
