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
    @UuidGenerator(style = UuidGenerator.Style.TIME) // 만드는 시점의 시간을 포함해서 uuid를 만든다.(uuid 긴문자열)
    private String apiKey;

    // name을 어떤 걸로 할지 여기서 정한다. nickname 또는 username
    public String getName() {
        return nickname;
    }
}
