package com.wk;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.wk.mapper")
public class VueblogJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VueblogJavaApplication.class, args);
	}

	/**
	 * MyBatisPlus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor(){
		return new PaginationInterceptor();
	}
}
