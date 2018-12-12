package aq.service.report.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.dao.report.CustomerRDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.report.CustomerRService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceCustomerR")
@DyncDataSource
public class CustomerRServiceImpl extends BaseServiceImpl  implements CustomerRService {

    @Resource
    private CustomerRDao customerRDao;

    @Override
    public JsonObject queryCustomerReport(JsonObject jsonObject) {
        jsonObject.addProperty("service","CustomerR");
        if(!StringUtil.isEmpty(jsonObject.get("noPage")) && "true".equals(jsonObject.get("noPage").getAsString())) {
            return queryNoPage(jsonObject,(map)->{
                List<Map<String, Object>> maps = customerRDao.selectCustomerReport(map);
                maps.forEach(obj-> {
                    if(!StringUtil.isEmpty(obj.get("address"))) {
                        String address = obj.get("address").toString().replace("=",":");
                        JsonObject addressJson = GsonHelper.getInstanceJsonparser().parse(address).getAsJsonArray().get(0).getAsJsonObject();
                        obj.put("address",StringUtil.isEmpty(addressJson.get("areaString"))?"":addressJson.get("areaString").getAsString());
                    }
                });
                return maps;
            });
        }else {
            return query(jsonObject,(map)->{
                List<Map<String, Object>> maps = customerRDao.selectCustomerReport(map);
                maps.forEach(obj-> {
                    if(!StringUtil.isEmpty(obj.get("address"))) {
                        String address = obj.get("address").toString().replace("=",":");
                        JsonObject addressJson = GsonHelper.getInstanceJsonparser().parse(address).getAsJsonArray().get(0).getAsJsonObject();
                        obj.put("address",StringUtil.isEmpty(addressJson.get("areaString"))?"":addressJson.get("areaString").getAsString());
                    }
                });
                return maps;
            });
        }
    }
}
