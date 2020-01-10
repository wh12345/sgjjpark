package com.qy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qy.entity.ParkEntity;

import java.util.Map;

/**
 * Created by Because of you on 2019/12/30.
 */
public interface ParkService extends IService<ParkEntity>{
    public void execuetWemFunction(Map<String,String> map);
}
