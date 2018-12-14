package aq.service.report.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.dao.report.CustomerRDao;
import aq.dao.report.ReportDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.report.CustomerRService;
import aq.service.report.ReportService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceReport")
@DyncDataSource
public class ReportServiceImpl extends BaseServiceImpl  implements ReportService {

    @Resource
    private ReportDao reportDao;

    @Override
    public JsonObject queryOrderReport(JsonObject jsonObject) {
        Rtn rtn = new Rtn("orderReport");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> res = new HashMap<>();
        List<Map<String, Object>> req = new ArrayList();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> orders = reportDao.selectOrderReport(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonArray();
        data.addProperty("total",jsonArray.size());
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
