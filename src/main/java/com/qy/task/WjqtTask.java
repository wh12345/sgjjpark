package com.qy.task;

import com.qy.entity.ParkEntity;
import com.qy.service.WjqtService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/3.
 */
@Component("wjqtTask")
public class WjqtTask extends ParkTask{
    @Autowired
    private WjqtService wjqtService;
    @Override
    public void execute(String params) {
        super.execute("武江桥头正在被执行，参数为："+params);
        Map<String,String> map = SysUtils.parseParams(params);
        List<ParkEntity> list = wjqtService.queryParks(map,this.sysConfig.getImgneturl());
        if(list!=null&&list.size()>0) {
            this.parkService.saveBatch(list);
        }
    }
}
