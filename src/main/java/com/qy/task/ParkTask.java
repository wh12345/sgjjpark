package com.qy.task;

import com.qy.config.SysConfig;
import com.qy.service.ParkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 停车场任务规范
 * Created by Because of you on 2019/12/30.
 */
public abstract class ParkTask{
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected Map<String,String> map;
    @Autowired
    protected SysConfig sysConfig;
    @Autowired
    protected ParkService parkService;
    /**
     * 有参数执行([key=value,.........])
     * @param params
     */
    public void execute(String params){
        log.info(params);
    }
}
