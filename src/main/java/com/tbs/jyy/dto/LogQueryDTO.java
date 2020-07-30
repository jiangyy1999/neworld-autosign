package com.tbs.jyy.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tbs-jyy
 * @description 复杂查询
 * @date 2020/7/30
 */
@Data
public class LogQueryDTO implements Serializable {
    private Integer userId;
    private Date startDate;
    private int page = 1;
}
