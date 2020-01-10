package com.qy.task;

import com.qy.entity.ParkEntity;
import com.qy.service.NxsgnjService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/2.
 */
@Component("nxsgnjTask")
public class NxsgnjTask extends ParkTask {
    @Autowired
    private NxsgnjService nxsgnjService;
    @Override
    public void execute(String params) {
        super.execute("南雄市公安局停车场正在被执行，参数为：" + params);
        Map<String,String> map = SysUtils.parseParams(params);
        List<ParkEntity> list =  nxsgnjService.queryWebParks(map,this.sysConfig.getImgneturl());
        if(list!=null&&list.size()>0) {
            this.parkService.saveBatch(list);
        }
    }
}
