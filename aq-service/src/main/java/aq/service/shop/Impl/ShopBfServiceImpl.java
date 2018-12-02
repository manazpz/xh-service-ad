package aq.service.shop.Impl;


import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.config.ConfigDao;
import aq.dao.resource.ResourceDao;
import aq.dao.shop.ShopDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.resource.ResourceService;
import aq.service.shop.ShopBfService;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceBfShop")
@DyncDataSource
public class ShopBfServiceImpl extends BaseServiceImpl  implements ShopBfService {

    @Resource
    private ShopDao shopDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ConfigDao configDao;

    @Resource
    private ResourceDao resourceDao;

    @Resource
    protected ResourceService resourceService;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryShop(JsonObject jsonObject) {
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Rtn rtn = new Rtn("shop");
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("userId", user.getUserId());
            List<Map<String, Object>> lists = shopDao.selectShop(res);
            rtn.setCode(200);
            rtn.setMessage("success");
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(lists)).getAsJsonArray();
            data.add("items",jsonArray);
            rtn.setData(data);
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateShop(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("lastCreateTime",new Date());
        if(((List)res.get("files")).size() > 0) {
            res.put("img",((Map)((List)res.get("files")).get(0)).get("url"));

        }
        shopDao.updateShop(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject querySettlement(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        jsonObject.addProperty("service","shop");
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.put("userId", user.getUserId());
            List<Map<String, Object>> lists = shopDao.selectShop(res);
            if(lists.size() > 0) {
                jsonObject.addProperty("shopId",lists.get(0).get("id").toString());
                return query(jsonObject,(map)->{
                    return shopDao.selectSettlement(map);
                });
            }else {
                rtn.setCode(404);
                rtn.setMessage("店铺不存在！");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject insertShop(HttpServletRequest request, JsonObject jsonObject) throws Exception{
        Rtn rtn = new Rtn("shop");
        String shopId = UUIDUtil.getUUID();
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("platform","OSS");
        List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
        if(tpp.size() < 1) {
            rtn.setCode(404);
            rtn.setMessage("OSS未配置！");
        }else {
            String userId = UUIDUtil.getUUID();
            Map<String, Object> tppMap = tpp.get(0);
            ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(), tppMap.get("accessKeyId").toString(), tppMap.get("accessKeySecret").toString());
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> paperImgs = multipartRequest.getMultiFileMap().get("paperImgs");
            if (paperImgs!=null&&paperImgs.size() > 0) {
                JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, paperImgs, jsonObject.get("paperPath").getAsString(),tppMap.get("towPath").toString(), "", tppMap.get("backetName").toString());
                if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                    jsonObject.add("paperFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
            }
            List<MultipartFile> shopImgs = multipartRequest.getMultiFileMap().get("shopImgs");
            if (shopImgs!=null&&shopImgs.size() > 0) {
                JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, shopImgs, jsonObject.get("shopPath").getAsString(),tppMap.get("towPath").toString(), shopId, tppMap.get("backetName").toString());
                if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                    jsonObject.add("shopFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
            }
            List<MultipartFile> userSps = multipartRequest.getMultiFileMap().get("userSps");
            if (userSps!=null&&userSps.size() > 0) {
                JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, userSps, jsonObject.get("userPath").getAsString(),tppMap.get("towPath").toString(), "", tppMap.get("backetName").toString());
                if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                    jsonObject.add("spFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
            }
            res.clear();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            if(!StringUtil.isEmpty(res.get("user"))) {
                Map userMap = GsonHelper.getInstance().fromJson(res.get("user").toString(), Map.class);
                //更新用户信息
                userMap.put("id", userId);
                userMap.put("nickName", userMap.get("name"));
                userMap.put("userName", userMap.get("phone"));
                userMap.put("status", "SD");
                userMap.put("createTime",new Date());
                userMap.put("updateTime",new Date());
                userMap.put("password", MD5.getMD5String(userMap.get("password").toString()));
                userDao.insertUserInfo(userMap);
                //插入店铺信息
                if(!StringUtil.isEmpty(res.get("shop"))) {
                    Map shopMap = GsonHelper.getInstance().fromJson(res.get("shop").toString(), Map.class);
                    shopMap.put("id",shopId);
                    shopMap.put("no", UUIDUtil.getRandomReqNumber());
                    shopMap.put("userId", userMap.get("id"));
                    shopMap.put("createTime",new Date());
                    shopMap.put("lastCreateTime",new Date());
                    if(!StringUtil.isEmpty(shopMap.get("foundingTime")) && shopMap.get("foundingTime").toString().indexOf("Z") != -1)
                        shopMap.put("foundingTime", DateTime.compareDate(shopMap.get("foundingTime").toString()));
                    if(!StringUtil.isEmpty(res.get("shopFiles")))
                        shopMap.put("img", ((List<Map>)res.get("shopFiles")).get(0).get("url"));
                    shopDao.insertShop(shopMap);
                }
                //插入证件信息
                if(!StringUtil.isEmpty(res.get("paper"))) {
                    String paperId = UUIDUtil.getUUID();
                    Map paperMap = GsonHelper.getInstance().fromJson(res.get("paper").toString(), Map.class);
                    paperMap.put("id",paperId);
                    paperMap.put("basicId",userMap.get("id"));
                    paperMap.put("createTime",new Date());
                    paperMap.put("updateTime",new Date());
                    if(!StringUtil.isEmpty(paperMap.get("time"))) {
                        if(((List)paperMap.get("time")).get(0).toString().indexOf("Z") != -1 && ((List)paperMap.get("time")).get(1).toString().indexOf("Z") != -1) {
                            paperMap.put("startTime",DateTime.compareDate(((List)paperMap.get("time")).get(0).toString()));
                            paperMap.put("endTime",DateTime.compareDate(((List)paperMap.get("time")).get(1).toString()));
                        }
                    }
                    userDao.insertUsersPaper(paperMap);
                }
                //插入银行信息
                if(!StringUtil.isEmpty(res.get("bank"))) {
                    String bankId = UUIDUtil.getUUID();
                    Map bankMap = GsonHelper.getInstance().fromJson(res.get("bank").toString(), Map.class);
                    bankMap.put("id",bankId);
                    bankMap.put("basicId",userMap.get("id"));
                    bankMap.put("createTime",new Date());
                    bankMap.put("updateTime",new Date());
                    userDao.insertUsersBank(bankMap);
                }
                //添加证件资源
                if(!StringUtil.isEmpty(res.get("paperFiles"))) {
                    List<Map> paperFiles = (List<Map>) res.get("paperFiles");
                    for(Map obj : paperFiles) {
                        obj.put("type","ZP");
                        obj.put("refId",userMap.get("id"));
                        obj.put("createUserId", userId);
                        obj.put("lastCreateUserId", userId);
                        obj.put("createTime", new Date());
                        obj.put("lastCreateTime", new Date());
                        resourceDao.insertResourcet(obj);
                    }
                }
                //添加身份证
                if(!StringUtil.isEmpty(res.get("spFiles"))) {
                    List<Map> spFiles = (List<Map>) res.get("spFiles");
                    for(Map obj : spFiles) {
                        obj.put("type","SP");
                        obj.put("refId",userMap.get("id"));
                        obj.put("createUserId", userId);
                        obj.put("lastCreateUserId", userId);
                        obj.put("createTime", new Date());
                        obj.put("lastCreateTime", new Date());
                        resourceDao.insertResourcet(obj);
                    }
                }
                rtn.setCode(200);
                rtn.setMessage("success");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
