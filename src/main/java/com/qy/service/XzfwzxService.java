package com.qy.service;

import com.qy.entity.ParkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/3.
 */
public interface XzfwzxService {
    public List<ParkEntity> queryParks(Map<String,String> map, String imgneturl);
}
