package aq.service.report;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface CustomerRService extends BaseService {

    //客户报表
    JsonObject queryCustomerReport(JsonObject jsonObject);

}
