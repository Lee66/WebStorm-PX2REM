package com.sunq.constvalue;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.sunq.utils.LogicUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author sunqian
 * @date 2018/8/8
 */
@State(name = "px2remForWebStorm",storages = {@Storage("px2remforwebstorm.xml")})
public class ConstValue implements PersistentStateComponent<ConstValue> {

    private LogicUtils logic = LogicUtils.getLogic();

    public String remBaseValue;

    public Boolean showCalculationProcess;

    public Boolean getShowCalculationProcess() {
        return this.logic.funOrElse(showCalculationProcess, Objects::nonNull, show -> show, show -> false);
    }

    public void setShowCalculationProcess(Boolean showCalculationProcess) {
        this.showCalculationProcess = showCalculationProcess;
    }

    @Override
    public ConstValue getState() {
        return this;
    }

    @Override
    public void loadState(ConstValue state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public double getRemBaseValue() {
        return Double.parseDouble(remBaseValue == null ? "100.0" : remBaseValue);
    }

    public void setRemBaseValue(String remBaseValue) {
        this.remBaseValue = remBaseValue;
    }

    @Nullable
    public static ConstValue getInstance(Project project) {
        return ServiceManager.getService(project, ConstValue.class);
    }
}
