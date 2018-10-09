package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.system.SystemDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.AddressService;
import aq.service.system.Func;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceAddress")
@DyncDataSource
public class AddressServiceImpl extends BaseServiceImpl implements AddressService {

    @Resource
    private SystemDao systemDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertAddress(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Address");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        Map<String,Object> rests = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("id", res.get("openId"));
        List<Map<String, Object>> maps = systemDao.selectAddress(rest);
        if(maps.size()>0){
            rest.put("no",maps.size()*10+10);
        }else{
            rest.put("no",10);
        }
        rest.put("name", res.get("name"));
        rest.put("phone", res.get("phone"));
        rest.put("areaString", res.get("areaString"));
        rest.put("streetString", res.get("streetString"));
        if("Y".equals(res.get("isVisit").toString())){
            for (int i=0;i<maps.size();i++) {
                if("Y".equals(maps.get(i).get("isVisit"))){
                    rests.put("id",maps.get(i).get("id"));
                    rests.put("no",maps.get(i).get("no"));
                    rests.put("isVisit","N");
                    systemDao.updateAdress(rests);
                }
            }
        }
        rest.put("isVisit", res.get("isVisit").toString());
        systemDao.insertAddress(rest);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject queryAddress(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Address");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject json =new JsonObject();
        res.clear();
        res.put("id",jsonObject.get("openId").getAsString());
        res.put("isVisit",jsonObject.get("isVisit")== null ? "" :jsonObject.get("isVisit").getAsString());
        List<Map<String, Object>> list = systemDao.selectAddress(res);
        if(list.size()>0) {
            list.get(0).put("areaString",list.get(0).get("areaString").toString().replace(",",""));
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteAdress(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Address");
        Map<String,Object> res = new HashMap<>();
        List<Map<String, Object>> mapall = null;
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",jsonObject.get("id").getAsString());
        res.put("no",jsonObject.get("no").getAsString());
        systemDao.deleteAdress(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

}
