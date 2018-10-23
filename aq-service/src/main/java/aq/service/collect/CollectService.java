package aq.service.collect;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface CollectService extends BaseService {

    //查询收藏
    JsonObject queryCollect(JsonObject jsonObject);

}
