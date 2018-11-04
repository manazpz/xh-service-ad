package aq.service.statistics;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface StatisticsBfService extends BaseService {

    //总统计信息
    JsonObject statisticsOrderType(JsonObject jsonObject);

    //昨日热销商品
    JsonObject queryZrSaleGoods(JsonObject jsonObject);

    //昨日状况
    JsonObject queryZrStatus(JsonObject jsonObject);

}
