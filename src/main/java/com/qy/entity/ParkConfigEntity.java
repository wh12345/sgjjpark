package com.qy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * Created by Because of you on 2019/12/30.
 */
@Data
@TableName("park_config")
public class ParkConfigEntity {
    @TableId(value = "job_Id")
    private Long parkId;

    private String tccbh;

    private String tcwz;

    private String imgbasepath;

    private String dbname;

    private String driver;

    private String url;

    private String username;

    private String password;

    @TableField(value = "create_time")
    private Date createTime;

    private String otherproperties;

    private  String remark;


}
