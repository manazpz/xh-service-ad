package aq.service.customer;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface CustomerApiService extends BaseService {

    //查询收藏
    JsonObject queryCustomer(JsonObject jsonObject);

}
