package com.gogoal.common.common;

import java.util.Collection;

/**
 * @author wangjd on 2017/12/5 0005.
 * @description :${annotated}.
 */
public class ArrayUtils {

    public static boolean isEmpty(Collection<?> stack) {
        return stack == null || stack.isEmpty();
    }
}
