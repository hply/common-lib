package com.gogoal.base;

/**
 * @author wangjd on 2017/12/13 10:24.
 * @description :Json解析引擎
 */
public interface JsonEngine {

    /**
     * 实体类转json
     * */
    String toJson(Object o);

    <T> T parseObject(String json, Class<T> clzz);
}
