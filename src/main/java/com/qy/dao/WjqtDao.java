package com.qy.dao;

import com.qy.entity.ParkEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Because of you on 2020/1/3.
 */
@Mapper
public interface WjqtDao {
    public List<ParkEntity> queryParks(@Param("dates") String[] dates);
}
