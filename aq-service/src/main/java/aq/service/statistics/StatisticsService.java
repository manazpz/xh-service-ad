package aq.service.statistics;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface StatisticsService extends BaseService {

    //总统计信息
    JsonObject statisticsInfo(JsonObject jsonObject);

    //周总统计信息
    JsonObject statisticsWeekInfo(JsonObject jsonObject);

    //订单3大类型统计
    JsonObject statisticsOrderType(JsonObject jsonObject);

    //半年订单统计
    JsonObject statisticsendOrderMoon(JsonObject jsonObject);

    //3年订单统计
    JsonObject statisticsendOrderYear(JsonObject jsonObject);

    //获取客户
    JsonObject queryCustomer(JsonObject jsonObject);

    //获取标签商品
    JsonObject queryLableGoods(JsonObject jsonObject);

    //昨日热销商品
    JsonObject queryZrSaleGoods(JsonObject jsonObject);
}
