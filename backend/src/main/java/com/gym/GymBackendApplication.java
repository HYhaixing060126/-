package com.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class GymBackendApplication {
    public static void main(String[] args) { SpringApplication.run(GymBackendApplication.class, args); }

    /** 每次启动都用 Java UTF-8 字符串修复演示数据，避免旧数据库中的乱码继续显示。 */
    @Bean
    CommandLineRunner repairDemoText(JdbcTemplate db) {
        return args -> {
            db.update("update sys_user set real_name=?, role=? where username=?", "管理员", "超级管理员", "admin");
            db.update("update sys_user set real_name=?, role=? where username=?", "普通会员", "普通用户", "member");
            db.update("update sys_user set real_name=?, role=? where username=?", "尊贵会员", "VIP会员", "vip");
            db.update("update member set name=?, phone=?, card_type=? where id=1", "张小雨", "13800001024", "年卡");
            db.update("update member set name=?, phone=?, card_type=? where id=2", "李明远", "13900002251", "季卡");
            db.update("update member set status=?, expire_date=? where id=1", "正常", "2025-06-24");
            db.update("update member set status=?, expire_date=? where id=2", "即将到期", "2026-07-02");
            db.update("update gym_class set name=?, coach=? where id=1", "瑜伽基础", "李教练");
            db.update("update gym_class set name=?, coach=? where id=2", "力量训练", "王教练");
            db.update("update gym_class set name=?, coach=? where id=3", "动感单车", "张教练");
        };
    }
}
