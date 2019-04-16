package com.example.demo.mappers;
import org.springframework.stereotype.Repository;

import com.example.demo.base.mappers.BaseMapperImpl;
import com.example.demo.xiaochong.models.MobileInfo;


@Repository("mobileInfoDao")
public class MobileInfoMapper extends BaseMapperImpl<MobileInfo, Long> {
}
