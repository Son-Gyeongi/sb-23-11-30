package com.ll.sb231130.domain.member.member.entity;

import com.ll.sb231130.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    // name을 어떤 걸로 할지 여기서 정한다. nickname 또는 username
    public String getName() {
        return nickname;
    }
}
