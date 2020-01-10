package com.qy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qy.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 */
@Mapper
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {

}
