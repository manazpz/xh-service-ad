package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.DynamicEnumUtils;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.config.ConfigDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.ConfigService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import aq.common.constants.APPConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceConfig")
@DyncDataSource
public class ConfigServiceImpl extends BaseServiceImpl  implements ConfigService {

    @Resource
    private ConfigDao configDao;

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
        JsonArray jsonArray = new JsonArray();
        Map<String,Object> map = new HashMap<>();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        map.put("platform","wx");
        List<Map<String, Object>> results = configDao.selectTppConfig(map);
        if(results.size()>0){
            //微信公众账号支付配置
            WxPayH5Config wxPayH5Config = new WxPayH5Config();
            wxPayH5Config.setAppId(results.get(0).get("accessKeyId").toString());
            wxPayH5Config.setAppSecret(results.get(0).get("accessKeySecret").toString());
            wxPayH5Config.setMchId(results.get(0).get("mchid").toString());
            wxPayH5Config.setMchKey(results.get(0).get("keyword").toString());
            wxPayH5Config.setNotifyUrl("http://47.106.177.213:8083/aq/api/wx/notify");
            //支付类, 所有方法都在这个类里
            BestPayServiceImpl bestPayService = new BestPayServiceImpl();
            bestPayService.setWxPayH5Config(wxPayH5Config);
            //请求参数
            PayRequest payRequest = new PayRequest();
//            payRequest.setOpenid(map.get("openid").toString());
            payRequest.setOpenid("oaCWN0ns9o_IjsXbeRQtAqIeHhhg");
            payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
//            payRequest.setOrderAmount(Double.parseDouble(map.get("price").toString()));
            payRequest.setOrderAmount(0.01);
            payRequest.setOrderName(map.get("orderName").toString());
            payRequest.setOrderId(UUIDUtil.getRandomOrderId());
            payRequest.setSpbillCreateIp(APPConstants.SPBILL_CREATE_IP);
            //发起支付
            PayResponse pay = bestPayService.pay(payRequest);
            data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(pay)).getAsJsonObject();
            //异步回调
//            bestPayService.asyncNotify("http://yxh.huanyibu.com/");
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
