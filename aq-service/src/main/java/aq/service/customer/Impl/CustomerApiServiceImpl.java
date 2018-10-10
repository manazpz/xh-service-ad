package aq.service.customer.Impl;

import aq.common.annotation.DyncDataSource;

import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.dao.user.CustomerDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.customer.CustomerApiService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceCustomerApi")
@DyncDataSource
public class CustomerApiServiceImpl extends BaseServiceImpl  implements CustomerApiService {

    @Resource
    private CustomerDao customerDao;


    @Override
    public JsonObject queryCustomer(JsonObject jsonObject) {
        Rtn rtn = new Rtn("customer");
        HashMap req = new HashMap();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        req.clear();
        req.put("openId",jsonObject.get("openId").getAsString());
        List list = customerDao.selectCustomerInfo(req);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.addProperty("total",list.size());
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
