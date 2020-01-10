package com.qy.task;

import com.qy.entity.ParkEntity;
import com.qy.service.XzfwzxService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/3.
 */
@Component("xzfwzxTask")
public class XzfwzxTask extends ParkTask {
    @Autowired
    private XzfwzxService xzfwzxService;
    @Override
    public void execute(String params) {
        super.execute("行政服务中心正在被执行，参数为："+params);
        Map<String,String> map = SysUtils.parseParams(params);
        List<ParkEntity> list = xzfwzxService.queryParks(map,this.sysConfig.getImgneturl());
        if(list!=null&&list.size()>0) {
            this.parkService.saveBatch(list);
        }
    }
}
