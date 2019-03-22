package com.sunq.utils;

import clojure.lang.Obj;
import com.sunq.constvalue.ConstValue;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sunq.constvalue.CommonValue.*;

/**
 * @author sunqian
 * @date 2018/12/8 15:39
 * description 转换工具类
 */
public class FormatTools {

    private ConstValue constValue;
    private LogicUtils logic = LogicUtils.getLogic();

    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_PATTERN_VALUE);

    public FormatTools(ConstValue constValue) {
        this.constValue = constValue;
    }

    private String getFormatText(String ele) {
        return this.logic.funOrThrow(this.constValue.getRemBaseValue(), value -> Objects.nonNull(value) && value != 0, value ->
                        this.logic.funOrElse(ele, element -> Objects.isNull(element) || StringUtils.containsNone(element, STYLE_TAG) || Objects.equals(element, NULL_STRING), element -> element, element ->
                                this.logic.funOrElse(Double.valueOf(element.substring(0, element.indexOf(STYLE_TAG)).trim()), px -> Objects.nonNull(px) && check(px, value), px ->
                                        ((px / value) + "").replaceAll(PATTERN_REMOVE_END_ZERO, NULL_STRING) + REM_TAG + showComment(px, this.constValue.getRemBaseValue()), px ->
                                        String.format(this.getAccuracy(Double.toString(value)), (px / value)).replaceAll(PATTERN_REMOVE_END_ZERO, NULL_STRING).trim() + REM_TAG + showComment(px, value))
                        )
                , new RuntimeException(CONST_VALUE_NULL));
    }

    public String getFormatLine(String content) {
        int index = -1;
        while ((index = StringUtils.indexOf(content.toLowerCase(), STYLE_TAG)) > -1) {
            int startIndex = index;
            while (this.isNumeric(content.substring(startIndex - 1, index)) && startIndex > 0) {
                startIndex--;
            }
            if (startIndex != index) {
                String value = content.substring(startIndex, index) + STYLE_TAG;
                content = content.substring(0, startIndex) + getFormatText(value) + content.substring(index + 2);
            } else {
                break;
            }
        }

        return content;
    }

    /**
     * 匹配是否为数字
     *
     * @param str 判断字符串
     * @return 返回是否是数字
     */
    private boolean isNumeric(String str) {
        return this.logic.funOrEndWithEx(str, Objects::nonNull, value ->
                        NUMBER_PATTERN.matcher(new BigDecimal(str).toString()).matches()
                , value -> false, false);
    }

    /**
     * 检查是否可以被除尽
     *
     * @param amount 被除数
     * @param count  除数
     * @return 是否可以被除尽
     */
    public boolean check(double amount, double count) {
        return this.logic.funOrElse(count, divisor -> Objects.nonNull(divisor) && divisor != 0, divisor ->
                        this.logic.funOrElse((amount / divisor), value -> Objects.nonNull(value) && value == 0, value -> true, value -> {
                            return this.logic.funWithWhile(this.logic.funWithWhile(divisor, m -> m % 2 == 0, m -> m /2), n -> n % 5 == 0, n -> n /5);


//                            return this.logic.funOrElse((amount % m), flag -> Objects.nonNull(flag) && flag != 0, flag -> false, flag -> true);
                        })
                , divisor -> {
                    throw new RuntimeException(CONST_VALUE_NULL);
                });
    }

    public String showComment(Object obj1, Object obj2) {
        return this.logic.funOrElse(constValue.getShowCalculationProcess(), show -> show, show -> "  /* " + obj1.toString().replaceAll(PATTERN_REMOVE_END_ZERO, NULL_STRING) + "/" + obj2.toString().replaceAll(PATTERN_REMOVE_END_ZERO, NULL_STRING) + " */", show -> "");
    }

    /**
     * 获取精度
     * @param remValue  转换后的长度值
     * @return          返回转换精度后的值
     */
    public String getAccuracy(String remValue) {
        return this.logic.funOrElse(this.logic.funOrElse(remValue, value -> Objects.nonNull(value) && value.indexOf(STYLE_SEPARATE) < 0, value -> value.length(), value -> value.indexOf(STYLE_SEPARATE)), index -> Objects.nonNull(index) && index > 0, index ->
                MessageFormat.format(PATTERN_ACCURACY, remValue.substring(0, index.intValue()).length() + 1), index -> remValue);
    }

}
