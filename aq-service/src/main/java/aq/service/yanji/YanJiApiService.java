package aq.service.yanji;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface YanJiApiService extends BaseService {

    //验机
    JsonObject yanJi(JsonObject jsonObject);

}
