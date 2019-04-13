package com.sljm.svnadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sljm.svnadmin.handler.Interceptor1;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.sljm.svnadmin.mapper")
@EnableScheduling
public class SvnadminApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(SvnadminApplication.class, args);
	}
	@Override
    public void addInterceptors(InterceptorRegistry registry){
		//判断所有操作
		InterceptorRegistration ir=registry.addInterceptor(new Interceptor1());
        ir.addPathPatterns("/**");
        ir.excludePathPatterns("/logOut","/login","/shell/**","/errorInfo","/doLogin","/js/**","/i/**","/fonts/**","/html/**","/image/**","/css/**","/error/**");
    }
}
