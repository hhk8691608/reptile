package com.example.demo.Controller.OutInterface;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexControlle {

	@ResponseBody
	@RequestMapping("/index")
	public Map<String,String> index(){
		
		Map<String,String> result = new HashMap<>();
		result.put("code", "200");
		result.put("msg", "success");
		return result;
	}
	
}
