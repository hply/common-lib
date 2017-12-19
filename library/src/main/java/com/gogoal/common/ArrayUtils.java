package com.gogoal.common;

import java.util.Collection;

/**
 * author wangjd on 2017/8/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class ArrayUtils {
    public static boolean isEmpty(Collection list){
        if (list==null || list.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * 集合拼Srtring
     */
    public static String appendElements(Collection<String> myStockArr) {
        StringBuilder builder = new StringBuilder();
        for (String stockCode : myStockArr) {
            builder.append(stockCode);
            builder.append(";");
        }
        if (builder.length() > 0) {
            return builder.toString().substring(0, builder.length() - 1);
        } else {
            return "";
        }
    }
}
