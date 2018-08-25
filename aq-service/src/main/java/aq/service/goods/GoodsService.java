package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface GoodsService extends BaseService {

    //查询商品
    JsonObject queryGoods(JsonObject jsonObject);

    //查询机型
    JsonObject queryModel(JsonObject jsonObject);
}
