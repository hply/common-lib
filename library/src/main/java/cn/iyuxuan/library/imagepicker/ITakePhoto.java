package cn.iyuxuan.library.imagepicker;

import java.util.List;

/**
 * author wangjd on 2017/2/14 0014.
 * Staff_id 1375
 * phone 18930640263
 */
public interface ITakePhoto{
    void success(List<String> uriPaths, boolean isOriginalPic);
    void error();
}
