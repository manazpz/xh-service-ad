package aq.service.collect;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface CollectApiService extends BaseService {

    //添加收藏
    JsonObject insertCollect(JsonObject jsonObject);

    //取消收藏
    JsonObject deleteCollect(JsonObject jsonObject);

    //查询收藏
    JsonObject queryCollect(JsonObject jsonObject);

}
