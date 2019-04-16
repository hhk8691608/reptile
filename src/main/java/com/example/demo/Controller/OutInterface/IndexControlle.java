package com.example.demo.Controller.OutInterface;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dispatch.Comsumer;
import com.example.demo.services.MobileInfoService;
import com.example.demo.xiaochong.models.MobileInfo;

@RestController
public class IndexControlle {
	public static Logger LOG = LoggerFactory.getLogger(IndexControlle.class);
	
	
	@Autowired
	private MobileInfoService mobileInfoService;
	
	@ResponseBody
	@RequestMapping("/index")
	public Map<String,String> index(){
		LOG.info("index start .... ");
		Map<String,String> result = new HashMap<>();
		result.put("code", "200");
		result.put("msg", "success");
		LOG.info("index end .... ");
		return result;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/save")
	public Map<String,String> save(){
		LOG.info("save start .... ");
		MobileInfo entity = new MobileInfo();
		entity.setMobile("130000000");
		entity.setType("123");
		mobileInfoService.insert(entity);
		Map<String,String> result = new HashMap<>();
		result.put("code", "200");
		result.put("msg", "success");
		LOG.info("save end .... ");
		return result;
	}
	
}
