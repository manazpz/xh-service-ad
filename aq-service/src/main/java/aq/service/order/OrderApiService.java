package aq.service.order;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface OrderApiService extends BaseService {

    //查询订单列表
    JsonObject queryorderList(JsonObject jsonObject);

    //新增订单
    JsonObject instertorderList(JsonObject jsonObject);

    //查询订单数据
    List<Map<String, Object>> selectOrder(Map<String, Object> map);

    //更新订单
    int updateOrder(Map<String, Object> map);

}
