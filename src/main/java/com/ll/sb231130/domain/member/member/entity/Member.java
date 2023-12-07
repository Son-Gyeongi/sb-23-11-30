package com.ll.sb231130.domain.member.member.entity;

import com.ll.sb231130.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

@Entity
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String nickname;
    @Column(unique = true) // 유니크 인덱스로 검색 속도가 빠르다.
    @UuidGenerator(style = UuidGenerator.Style.RANDOM) // 랜덤으로 긴 문자열 만든다.
    private String apiKey;

    // name을 어떤 걸로 할지 여기서 정한다. nickname 또는 username
    public String getName() {
        return nickname;
    }
}
