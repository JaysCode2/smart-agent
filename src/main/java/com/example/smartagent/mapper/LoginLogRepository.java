package com.example.smartagent.mapper;

import com.example.smartagent.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 登录日志Repository接口
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    /**
     * 根据用户ID查询最近的登录记录
     */
    LoginLog findFirstByUserIdOrderByLoginTimeDesc(Long userId);
}
