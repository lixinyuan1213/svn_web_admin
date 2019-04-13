package com.sljm.svnadmin.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sljm.svnadmin.model.ResultMsg;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger Log = LogManager.getLogger();
	//声明要捕获的异常
		@ExceptionHandler(Exception.class)
		public Object defultExcepitonHandler(HttpServletRequest request,Exception e) {
			e.printStackTrace();
			if(e instanceof BusinessException) {
				Log.error(this.getClass()+"业务异常："+e.getMessage());
				BusinessException businessException = (BusinessException)e;
				ResultMsg resultMsg = ResultMsg.error(businessException.getMessage());
				return resultMsg;
			}
			//未知错误
			ResultMsg resultMsg = ResultMsg.error("未知错误");
			return resultMsg;
		}
}
