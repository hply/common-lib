package com.gogoal.common;

/**
 * @author wangjd on 2017/12/19 16:26.
 * @description :${annotated}.
 */
public class ExceptionUtils {
    public static void throwException(String exceprionMessage) {
        throw new IllegalArgumentException("Argument Exception" + exceprionMessage);
    }
}
