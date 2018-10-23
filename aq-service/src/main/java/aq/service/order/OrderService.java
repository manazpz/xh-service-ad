package aq.service.order;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface OrderService extends BaseService {

    //查询订单列表
    JsonObject queryorderList(JsonObject jsonObject);

    //查询订单明细列表
    JsonObject queryOrderDetail(JsonObject jsonObject);


}
