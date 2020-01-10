package com.qy.service.serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qy.dao.ScheduleJobLogDao;
import com.qy.entity.ScheduleJobLogEntity;
import com.qy.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

;

@Service("scheduleJobLogService")
@DS("oracle")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

}
