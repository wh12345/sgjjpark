package com.qy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.entity.ParkEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by Because of you on 2019/12/30.
 */
@Mapper
public interface ParkDao extends BaseMapper<ParkEntity>{
     public void execuetWemFunction(Map<String,String> map);
}
