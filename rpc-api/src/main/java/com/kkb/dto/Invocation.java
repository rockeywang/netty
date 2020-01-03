package com.kkb.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 客户端发送给服务端的服务调用信息
 * @author wangchao
 */
public class Invocation implements Serializable {
    /**
     * 接口名，即服务名称
     */
    private String className;
    /**
     * 远程调用的方法名
     */
    private String methodName;
    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 方法参数值
     */
    private Object[] paramValues;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object[] paramValues) {
        this.paramValues = paramValues;
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", paramValues=" + Arrays.toString(paramValues) +
                '}';
    }

}
