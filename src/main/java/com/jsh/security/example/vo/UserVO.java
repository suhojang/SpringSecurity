package com.jsh.security.example.vo;

import com.jsh.security.example.enums.role.UserRole;
import com.jsh.security.example.vo.common.CommonVO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Entity
@Table(name = "USER")
@Getter
public class UserVO extends CommonVO implements Serializable {
    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String userEmail;

    @Setter
    @Column(nullable = false)
    private String userPw;

    @Setter
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public UserVO(String userEmail, String userPw) {
        this.userEmail = userEmail;
        this.userPw = userPw;
    }
}
