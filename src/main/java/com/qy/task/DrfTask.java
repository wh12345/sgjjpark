package com.qy.task;

import com.qy.entity.ParkEntity;
import com.qy.service.DrfService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/6.
 */
@Component("drfTask")
public class DrfTask extends ParkTask {
    @Autowired
    private DrfService drfService;
    @Override
    public void execute(String params) {
        super.execute("大润发停车场任务正在被执行，参数为：" + params);
        Map<String,String> map = SysUtils.parseParams(params);
        List<ParkEntity> list = drfService.queryParks(map,this.sysConfig.getImgneturl());
        if(list!=null&&list.size()>0) {
            this.parkService.saveBatch(list);
        }
    }
}
