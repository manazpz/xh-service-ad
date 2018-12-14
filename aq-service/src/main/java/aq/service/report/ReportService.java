package aq.service.report;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ReportService extends BaseService {

    //订单报表
    JsonObject queryOrderReport(JsonObject jsonObject);

}
