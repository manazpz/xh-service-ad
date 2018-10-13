package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.config.ConfigDao;
import aq.dao.order.OrderDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.ConfigService;
import aq.service.system.Func;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import aq.common.constants.APPConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceConfig")
@DyncDataSource
public class ConfigServiceImpl extends BaseServiceImpl  implements ConfigService {

    @Resource
    private ConfigDao configDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryaConfig(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Config");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> map = new HashMap<>();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> results = configDao.selectConfig(map);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(results)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject pay(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Config");
        JsonObject data = new JsonObject();
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> listnew = new ArrayList();
        List<Map<String,Object>> listold = new ArrayList();
        String goodsName = "";
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if(map.get("newGoods") != null){
            listnew = GsonHelper.getInstance().fromJson(new Gson().toJson(map.get("newGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
        }
        if(map.get("newGoods") != null){
            listold = GsonHelper.getInstance().fromJson(new Gson().toJson(map.get("oldGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
        }
        map.put("platform","wx");
        List<Map<String, Object>> results = configDao.selectTppConfig(map);
        if(results.size()>0){
            //微信公众账号支付配置
            WxPayH5Config wxPayH5Config = new WxPayH5Config();
            wxPayH5Config.setAppId(results.get(0).get("accessKeyId").toString());
            wxPayH5Config.setAppSecret(results.get(0).get("accessKeySecret").toString());
            wxPayH5Config.setMchId(results.get(0).get("mchid").toString());
            wxPayH5Config.setMchKey(results.get(0).get("keyword").toString());
            wxPayH5Config.setNotifyUrl(APPConstants.WX_NOTIFY);
            //支付类, 所有方法都在这个类里
            BestPayServiceImpl bestPayService = new BestPayServiceImpl();
            bestPayService.setWxPayH5Config(wxPayH5Config);
            //请求参数
            PayRequest payRequest = new PayRequest();
            payRequest.setOpenid(map.get("openId").toString());
            payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
//            payRequest.setOrderAmount(Double.parseDouble(map.get("price").toString()));
            payRequest.setOrderAmount(0.01);
            if(listold.size()>0){
                for(int i=0; i< listold.size(); i++) {
                    goodsName += listold.get(i).get("goodsName")+ ";";
                }
            }
            if(listnew.size()>0){
                for(int i=0; i< listnew.size(); i++) {
                    goodsName += listnew.get(i).get("goodsName")+ ";";
                }
            }
            payRequest.setOrderName(goodsName);
            if(map.get("orderId") == null){
                payRequest.setOrderId(UUIDUtil.getRandomOrderId());
            }else{
                payRequest.setOrderId(map.get("orderId").toString());
            }
            payRequest.setSpbillCreateIp(APPConstants.SPBILL_CREATE_IP);
            //发起支付
            PayResponse pay = bestPayService.pay(payRequest);
            data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(pay)).getAsJsonObject();
            //插入订单信息
            if(map.get("orderId") == null){
                Map<String,Object> rest = new HashMap<>();
                Map<String,Object> restdetail = new HashMap<>();
                rest.put("openid",payRequest.getOpenid());
                List<Map<String, Object>> userinfo = userDao.selectUserInfos(rest);
                rest.put("id", UUIDUtil.getUUID());
                rest.put("number", payRequest.getOrderId());
                if(map.get("newGoods") != null){
                    listnew = GsonHelper.getInstance().fromJson(new Gson().toJson(map.get("newGoods")), new TypeToken<List<Map<String,Object>>>(){}.getType());
                    if(listnew.size()>0){
                        rest.put("shopid", listnew.get(0).get("shopid"));
                    }
                    rest.put("type", "02");
                    if(userinfo.size()>0){
                        rest.put("buyer",userinfo.get(0).get("id"));
                    }
                    rest.put("paystatus", "01");//付款状态 :01：未付款    02：已付款  03：已取消
                    rest.put("orderstatus","03");//订单状态:01：已完成    02：已取消  03：进行中   04：售后中
                    rest.put("deliverystatus","02");//收/发货状态://01：已发货    02：未发货    03：已收货
                    rest.put("price",map.get("price"));
                    rest.put("createTime",new Date());
                    rest.put("lastCreateTime",new Date());
                    //插入订单抬头信息
                    orderDao.insertOrder(rest);
                    //插入订单明细信息
                    restdetail.put("id",rest.get("id"));
                    List<Map<String, Object>> maps = orderDao.selectorderDetailList(restdetail);
                    for(int i=0; i< listnew.size(); i++) {
                        if(maps.size()>0){
                            restdetail.put("no",(maps.size()+i)*10+10);
                        }else{
                            restdetail.put("no",i*10 +10);
                        }
                        restdetail.put("goodsid",listnew.get(i).get("goodsId"));
                        restdetail.put("parameter",listnew.get(i).get("bllParameter"));
                        restdetail.put("createTime",new Date());
                        restdetail.put("lastCreateTime",new Date());
                        orderDao.insertOrderDetail(restdetail);
                    }
                }
            }
            //异步回调
//          bestPayService.asyncNotify("http://yxh.huanyibu.com/");
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject insertConfig(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject deleteConfig(JsonObject jsonObject) {
        return null;
    }

    @Override
    public JsonObject updateConfig(JsonObject jsonObject) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectTppConfig(Map<String, Object> map) {
        return  configDao.selectTppConfig(map);
    }
    @Override
    public void updateTppConfig(Map<String, Object> map) {
        configDao.updateTppConfig(map);
    }
}
