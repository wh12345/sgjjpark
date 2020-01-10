package com.qy.task;

import com.qy.entity.ParkEntity;
import com.qy.service.BshcService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/2.
 */
@Component("bshcTask")
public class BshcTask extends  ParkTask {
    @Autowired
    private BshcService bshcService;
    @Override
    public void execute(String params) {
        super.execute("碧水花城正在被执行，参数为：" + params);
        Map<String,String> map = SysUtils.parseParams(params);
        List<ParkEntity> list = bshcService.getParks(map,this.sysConfig.getImgneturl());
        if(list!=null&&list.size()>0) {
            this.parkService.saveBatch(list);
        }
    }
}
