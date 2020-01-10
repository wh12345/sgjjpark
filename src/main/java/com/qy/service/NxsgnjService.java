package com.qy.service;

import com.qy.entity.ParkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/2.
 */
public interface NxsgnjService {
    public List<ParkEntity> queryWebParks(Map<String,String> map,String imgneturl);

    public Map<String,String>  login(String url,String user,String pwd,String tess4jpath);
}
