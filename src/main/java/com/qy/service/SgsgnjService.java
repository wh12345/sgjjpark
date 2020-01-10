package com.qy.service;

import com.qy.entity.ParkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Because of you on 2019/12/30.
 */
public interface SgsgnjService  {
    public List<ParkEntity> queryParks(Map<String,String> map,String imgneturl);
}
