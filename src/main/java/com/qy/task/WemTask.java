package com.qy.task;

import com.qy.service.WemService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/1.
 */
@Component("wemTask")
public class WemTask extends ParkTask {
    @Autowired
    private WemService wemService;
    @Override
    public void execute(String params) {
        super.execute("沃尔码停车场正在被执行，参数为：" + params);
        Map<String,String> map = SysUtils.parseParams(params);
        Map<String,String> resultMap = wemService.getShareImg(map,this.sysConfig.getImgneturl());
        if(resultMap!=null) {
            this.parkService.execuetWemFunction(resultMap);
        }
    }
}
