package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qy.dao.ParkConfigDao;
import com.qy.entity.ParkConfigEntity;
import com.qy.service.ParkConfigService;
import org.springframework.stereotype.Service;

/**
 * Created by Because of you on 2019/12/30.
 */
@Service
@DS("oracle")
public class ParkConfigServiceImpl extends ServiceImpl<ParkConfigDao,ParkConfigEntity> implements ParkConfigService{
}
