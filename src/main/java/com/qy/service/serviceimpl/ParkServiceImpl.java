package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qy.dao.ParkDao;
import com.qy.entity.ParkEntity;
import com.qy.service.ParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Because of you on 2019/12/30.
 */
@Slf4j
@Service
@DS("oracle")
public class ParkServiceImpl extends ServiceImpl<ParkDao,ParkEntity> implements ParkService{
    @Autowired
    private ParkDao parkdao;
    @Override
    public void execuetWemFunction(Map<String, String> map) {
        parkdao.execuetWemFunction(map);
        log.info("沃尔玛停车场执行存储函数:imgname"+map.get("imgname")+" retcode"+map.get("retcode")+" cwxx_out"+map.get("cwxx_out"));
    }
}
