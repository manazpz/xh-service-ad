package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface OrderService extends BaseService {

    //查询订单列表
    JsonObject queryorderList(JsonObject jsonObject);


}
