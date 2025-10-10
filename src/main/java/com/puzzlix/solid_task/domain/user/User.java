package com.puzzlix.solid_task.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_table")
@ToString // 연관 관계 사용할 때 고통을 겪는 코드, 순환 참조 일으킴
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 사용하는 DB의 제약을 따라서 시퀀스 값으로 지정함
    private Long id;
    private String name;
    private String email;
    private String password;
}

// 파스칼 케이스???