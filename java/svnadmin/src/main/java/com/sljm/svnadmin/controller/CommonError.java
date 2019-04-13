package com.sljm.svnadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonError {
	@RequestMapping("/errorInfo")
	public String showError()
	{
		return "error";
	}
}
