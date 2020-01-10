package com.qy.dao;

import com.qy.entity.ParkEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Because of you on 2020/1/6.
 */
@Mapper
public interface DrfDao {
    public List<ParkEntity> queryParks(@Param("dates") String[] dates);
}
