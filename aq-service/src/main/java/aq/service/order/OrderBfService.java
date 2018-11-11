package aq.service.order;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface OrderBfService extends BaseService {

    //查询订单列表
    JsonObject queryorderList(JsonObject jsonObject);

    //查询订单明细列表
    JsonObject updateOrder(JsonObject jsonObject);

    //新增订单物流信息
    JsonObject insertLogistics(JsonObject jsonObject);


}
