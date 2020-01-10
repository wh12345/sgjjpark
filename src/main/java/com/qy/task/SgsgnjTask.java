package com.qy.task;

import com.qy.entity.ParkEntity;
import com.qy.service.SgsgnjService;
import com.qy.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2019/12/30.
 */
@Component("sgsgnjTask")
public class SgsgnjTask extends ParkTask{
    @Autowired
    private SgsgnjService sgsgnjService;
    @Override
    public void execute(String params){
        super.execute("韶关市公安局正在被执行，参数为：" + params);
        Map<String,String> map = SysUtils.parseParams(params);
        List<ParkEntity> list = sgsgnjService.queryParks(map,this.sysConfig.getImgneturl());
        this.parkService.saveBatch(list);
    }
}
