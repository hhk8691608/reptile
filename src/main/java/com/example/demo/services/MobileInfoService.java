package com.example.demo.services;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.example.demo.base.services.BaseServiceImpl;
import com.example.demo.mappers.MobileInfoMapper;
import com.example.demo.xiaochong.models.MobileInfo;

@Service("mobileInfoService")
public class MobileInfoService extends BaseServiceImpl<MobileInfo, Long, MobileInfoMapper>{
}
