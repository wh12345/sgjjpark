package com.qy.dao;

import com.qy.entity.ParkEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Because of you on 2019/12/30.
 */
@Mapper
public interface SgsgnjDao {
    public List<ParkEntity> queryParks(@Param("dates") String[] dates);
}
