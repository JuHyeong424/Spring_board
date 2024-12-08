package com.example.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


// 시간 정보 다루는 클래스
@MappedSuperclass // 다른 entity 클래스가 상속받아 사용할 공통 매핑 정보
@EntityListeners(AuditingEntityListener.class) // entity의 변경 사항을 감지하여 자동으로 필드 값을 처리하는 리스너
@Getter
public class BaseEntity {

    @CreationTimestamp // entity가 처음 생성될 때의 시간을 createdTime 필드에 자동 기록
    @Column(updatable = false) // 수정 시 무시
    private LocalDateTime createdTime;

    @UpdateTimestamp // entity가 수정될 때의 시간을 updatedTime 필드에 자동으로 업데이트
    @Column(insertable = false)  // 삽입 시 무시
    private LocalDateTime updatedTime;
}
