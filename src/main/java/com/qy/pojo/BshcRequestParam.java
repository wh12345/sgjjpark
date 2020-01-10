package com.qy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BshcRequestParam {
    private String appkey;
    private Long time;
    private Integer pageNo;
    private Integer pageSize;
    private String opUserUuid;
    private Long startTime;
    private Long endTime;
}