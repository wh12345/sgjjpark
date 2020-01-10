package com.qy.service;

import com.qy.entity.ParkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2020/1/2.
 */
public interface BshcService {
    public List<ParkEntity> getParks(Map<String,String> map,String imgneturl);
}
