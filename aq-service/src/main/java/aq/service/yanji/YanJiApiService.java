package aq.service.yanji;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface YanJiApiService extends BaseService {

    //获取验机数据
    JsonObject yanJi(JsonObject jsonObject);

    //插入验机
    JsonObject inseryYanJi(JsonObject jsonObject);

    //更新验机
    JsonObject updateYanJi(JsonObject jsonObject);

    //查询已验机数据
    JsonObject amslerList(JsonObject jsonObject);
}
