package com.example.demo.services;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.example.demo.base.services.BaseServiceImpl;
import com.example.demo.mappers.WebsiteInfoMapper;
import com.example.demo.xiaochong.models.WebsiteInfo;

@Service("websiteInfoService")
public class WebsiteInfoService extends BaseServiceImpl<WebsiteInfo, Long, WebsiteInfoMapper>{
}
