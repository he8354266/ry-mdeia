package com.ruoyi.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description //TODO
 * @Date 2024/2/28 13:50
 * @Author hy
 **/

@Configuration
@MapperScan("com.ruoyi.**.mapper")
public class MyBatisConfiguration {

}
