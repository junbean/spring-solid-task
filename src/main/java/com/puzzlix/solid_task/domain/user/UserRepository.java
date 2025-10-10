package com.puzzlix.solid_task.domain.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 메서드 쿼리들
}
