package com.qy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Created by Because of you on 2019/12/30.
 */
@Data
@NoArgsConstructor
@ToString
@TableName("t_jj_park")
public class ParkEntity {
    private String  id;
    @TableField(value="p_key")
    private String  pkey;
    private String  tccbh;
    private String  fxlx;
    private String  tcwz;
    private String  crkbh;
    private  Date   txsj;
    private String  hpzl;
    private String  hphm;
    private String  hpys;
    private String  tplj;
    private String scbj;
    private Date   scsj;
    private String rcid;
}
