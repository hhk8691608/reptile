package com.example.demo.mappers;
import org.springframework.stereotype.Repository;

import com.example.demo.base.mappers.BaseMapperImpl;
import com.example.demo.xiaochong.models.WebsiteInfo;


@Repository("websiteInfoDao")
public class WebsiteInfoMapper extends BaseMapperImpl<WebsiteInfo, Long> {
}
