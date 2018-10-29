package aq.service.invoice.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.common.util.UUIDUtil;
import aq.dao.invoice.InvoiceDao;
import aq.dao.order.OrderDao;
import aq.dao.shop.ShopDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.invoice.InvoiceBfService;
import aq.service.system.Func;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceInvoiceBf")
@DyncDataSource
public class InvoiceBfServiceImpl extends BaseServiceImpl  implements InvoiceBfService {

    @Resource
    private InvoiceDao invoiceDao;

    @Resource
    private OrderDao orderDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertInvoice(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("invoice");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("id", UUIDUtil.getUUID());
            res.put("createUser", user.getUserId());
            res.put("createTime", DateTime.compareDate(res.get("createTime").toString()));
            invoiceDao.insertInvoice(res);
            res.clear();
            res.put("lastCreateUser",user.getUserId());
            res.put("lastCreateTime",new Date());
            res.put("id",jsonObject.get("orderId").getAsString());
            res.put("deliverystatus","01");
            orderDao.updateOrder(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updateInvoice(JsonObject jsonObject) {
        Rtn rtn = new Rtn("invoice");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        invoiceDao.updateInvoice(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject selectInvoice(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("invoice");
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
            return Func.functionRtnToJsonObject.apply(rtn);
        }else {
            jsonObject.addProperty("service","invoice");
            jsonObject.addProperty("createUser",user.getUserId());
            return query(jsonObject,(map)->{
                return invoiceDao.selectInvoice(map);
            });
        }
    }

}
