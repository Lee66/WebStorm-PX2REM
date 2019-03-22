package com.sunq.utils;

import java.text.MessageFormat;

/**
 * @author sunqian
 * @date 2018/12/18
 */
public class FormatAccuracy {

    public String getAccuracy(String remValue){
        return MessageFormat.format("%.{0}f",remValue.substring(0, remValue.indexOf(".") < 0 ? remValue.length() : remValue.indexOf(".")).length()+1);
    }

}
