package com.tbs.jyy.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tbs-jyy
 * @description 日期格式转换
 * @date 2020/7/30
 */
public class DateConverter implements Converter<String, Date> {
    private final SimpleDateFormat simpleDateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date convert(String s) {
        if (!StringUtils.isEmpty(s)) {
            try {
                return simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
