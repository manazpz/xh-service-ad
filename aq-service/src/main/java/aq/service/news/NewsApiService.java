package aq.service.news;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface NewsApiService extends BaseService {

    //查询订单日志列表
    JsonObject queryOrderNewsList(JsonObject jsonObject);

    //新增订单日志
    int instertorderLog(Map<String, Object> map);

}
