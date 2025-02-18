package com.green.jobdone.entity;

import com.green.jobdone.config.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"email", "providerType"}
                )
        }
)
public class User extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 속성이 들어감.
    @Column(name = "userId")
    private Long userId;

    @Column(nullable = false)
    private SignInProviderType providerType;

    @Column(nullable = false, length = 40,unique = true) // not null, unique 속성 추가
    private String email;

    @Column(nullable = false,length = 100)
    private String upw;

    @Column(length = 30, nullable = false)
    private String name; // 자동으로 nick_name 이 된다

    @Column(length = 250)
    private String pic;

    @Column(length = 11, nullable = false)
    private String phone;

    @Column(nullable = false)
    @ColumnDefault("100")
    private int type; // 디폴트값으로 100으로 설정




}
