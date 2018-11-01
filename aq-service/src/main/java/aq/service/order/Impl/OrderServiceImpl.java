package aq.service.order.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.NumUtil;
import aq.common.util.StringUtil;
import aq.dao.order.OrderDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceOrder")
@DyncDataSource
public class OrderServiceImpl extends BaseServiceImpl  implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("order");
        jsonObject.addProperty("service","order");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            Map<String,Object> ress = new HashMap<>();
            return query(jsonObject,(map)->{
                List<Map<String, Object>> orders = orderDao.selectorderList(map);
                for (Map obj : orders) {
                    String flag = "";
                    res.put("id", obj.get("id"));
                    List<Map<String, Object>> orderPs = orderDao.selectorderDetailList(res);
                    for (Map obj1 : orderPs) {
                        ress.clear();
                        ress.put("refId", obj1.get("goodsId"));
                        List imgs = resourceDao.selectResource(ress);
                        obj1.put("imgs", imgs);
                        String str = "";
                        Double banPrice = Double.parseDouble(obj1.get("banPrice").toString());
                        if (!StringUtil.isEmpty(obj1.get("parameter"))) {
                            List<Map> parameter = GsonHelper.getInstance().fromJson(obj1.get("parameter").toString(), List.class);
                            Double sum = 0.0;
                            for (Map obj2 : parameter) {
                                List<Map> spec = (List<Map>) obj2.get("spec");
                                for (Map obj3 : spec) {
                                    str += obj3.get("spec_value_name") + " ";
                                    if ("02".equals(obj1.get("goodsModel"))) {
                                        Double correntPrice = Double.parseDouble(obj3.get("correntPrice").toString());
                                        Double minPrice = Double.parseDouble(obj3.get("minPrice").toString());
                                        Double cappedPrice = Double.parseDouble(obj3.get("cappedPrice").toString());
                                        Double price = banPrice + correntPrice;
                                        sum = sum + NumUtil.compareDouble(price, minPrice, cappedPrice) - banPrice;
                                    }
                                }
                                if ("01".equals(obj1.get("goodsModel"))) {
                                    obj1.put("guJia", obj2.get("price"));
                                }
                            }
                            if ("02".equals(obj1.get("goodsModel"))) {
                                if(StringUtil.isEmpty(flag)) {
                                    flag = "02";
                                }
                                obj1.put("guJia", - sum - banPrice);
                            }
                        }
                        if ("01".equals(obj1.get("checkStatus"))) {
                            flag = "01";
                        }
                        obj1.put("parameterStr", str);
                    }
                    obj.put("checkStatus", flag);
                    obj.put("goods",orderPs);
                    List address = GsonHelper.getInstance().fromJson(obj.get("address").toString(), List.class);
                    if(address != null){
                        obj.put("address",address.get(0));
                    }
                }
                return orders;
            });
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

}
