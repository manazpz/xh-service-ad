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
import aq.service.shop.ShopService;
import aq.service.system.Func;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceShop")
@DyncDataSource
public class ShopServiceImpl extends BaseServiceImpl  implements ShopService {

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
    public JsonObject insertShop(HttpServletRequest request,JsonObject jsonObject) throws Exception{
        Rtn rtn = new Rtn("shop");
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            String shopId = UUIDUtil.getUUID();
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("platform","OSS");
            List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
            if(tpp.size() < 1) {
                rtn.setCode(404);
                rtn.setMessage("OSS未配置！");
            }else {
                Map<String, Object> tppMap = tpp.get(0);
                ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(), tppMap.get("accessKeyId").toString(), tppMap.get("accessKeySecret").toString());
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                List<MultipartFile> paperImgs = multipartRequest.getMultiFileMap().get("paperImgs");
                if (paperImgs!=null&&paperImgs.size() > 0) {
                    JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, paperImgs, jsonObject.get("paperPath").getAsString(), "", tppMap.get("backetName").toString());
                    if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                        jsonObject.add("paperFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
                }
                List<MultipartFile> shopImgs = multipartRequest.getMultiFileMap().get("shopImgs");
                if (shopImgs!=null&&shopImgs.size() > 0) {
                    JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, shopImgs, jsonObject.get("shopPath").getAsString(), shopId, tppMap.get("backetName").toString());
                    if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                        jsonObject.add("shopFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
                }
                List<MultipartFile> userSps = multipartRequest.getMultiFileMap().get("userSps");
                if (userSps!=null&&userSps.size() > 0) {
                    JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, userSps, jsonObject.get("userPath").getAsString(), "", tppMap.get("backetName").toString());
                    if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                        jsonObject.add("spFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
                }
                res.clear();
                res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
                if(!StringUtil.isEmpty(res.get("user"))) {
                    Map userMap = GsonHelper.getInstance().fromJson(res.get("user").toString(), Map.class);
                    //更新用户信息
                    userMap.put("updateTime",new Date());
                    userDao.updateUserInfo(userMap);
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
                            obj.put("createUserId", user.getUserId());
                            obj.put("lastCreateUserId", user.getUserId());
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
                            obj.put("createUserId", user.getUserId());
                            obj.put("lastCreateUserId", user.getUserId());
                            obj.put("createTime", new Date());
                            obj.put("lastCreateTime", new Date());
                            resourceDao.insertResourcet(obj);
                        }
                    }
                    rtn.setCode(200);
                    rtn.setMessage("success");
                }
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryShop(JsonObject jsonObject) {
        jsonObject.addProperty("service","shop");
        return query(jsonObject,(map)->{
            return shopDao.selectShop(map);
        });
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryShopDetail(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> req = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("shopId").getAsString());
        List<Map<String, Object>> shop = shopDao.selectShop(res);
        if(shop.size()>0) {
            Map<String, Object> shopMap = shop.get(0);
            res.clear();
            res.put("id",shopMap.get("userId"));
            List<Map<String, Object>> user = userDao.selectUserInfo(res);
            res.clear();
            res.put("basicId",shopMap.get("userId"));
            List<Map<String, Object>> bank = userDao.selectUserBank(res);
            List<Map<String, Object>> paper = userDao.selectUsersPaper(res);
            res.clear();
            res.put("refId",shopMap.get("userId"));
            res.put("type","SP");
            List<Map<String, Object>> userSps = resourceDao.selectResource(res);
            res.clear();
            res.put("refId",shopMap.get("userId"));
            res.put("type","ZP");
            List<Map<String, Object>> paperImgs = resourceDao.selectResource(res);

            req.put("shop",shopMap);
            req.put("bank",bank.get(0));
            req.put("paper",paper.get(0));
            req.put("user",user.get(0));
            if(!StringUtil.isEmpty(shopMap.get("img"))) {
                List imgList = new ArrayList();
                Map<String,Object> imgMap = new HashMap<>();
                imgMap.clear();
                imgMap.put("url",shopMap.get("img"));
                imgList.clear();
                imgList.add(imgMap);
                req.put("shopImgs",imgList);
            }
            req.put("paperImgs",paperImgs);
            req.put("apaperImgs",paperImgs);
            req.put("userSps",userSps);
            req.put("auserSps",userSps);
            rtn.setCode(200);
            rtn.setMessage("success");
            JsonObject data = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(req)).getAsJsonObject();
            rtn.setData(data);
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateShop(HttpServletRequest request,JsonObject jsonObject) throws Exception{
        Rtn rtn = new Rtn("shop");
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("platform","OSS");
            List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
            if(tpp.size() < 1) {
                rtn.setCode(404);
                rtn.setMessage("OSS未配置！");
            }else {
                res.clear();
                res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
                if(!StringUtil.isEmpty(res.get("shop"))) {
                    Map shopMap = GsonHelper.getInstance().fromJson(res.get("shop").toString(), Map.class);
                    String shopId = (String) shopMap.get("id");
                    Map<String, Object> tppMap = tpp.get(0);
                    ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(), tppMap.get("accessKeyId").toString(), tppMap.get("accessKeySecret").toString());
                    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                    //店铺图片更新
                    List<MultipartFile> shopImgs = multipartRequest.getMultiFileMap().get("shopFiles");
                    if (shopImgs!=null&&shopImgs.size() > 0) {
                        JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, shopImgs, res.get("shopPath").toString(), shopId, tppMap.get("backetName").toString());
                        if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                            res.put("updateShopFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
                    }
                    //证件图片更新
                    List<MultipartFile> paperImgs = multipartRequest.getMultiFileMap().get("paperFiles");
                    List<Map> paperImgs1 = StringUtil.isEmpty(res.get("paperImgs"))? new ArrayList<>() : GsonHelper.getInstance().fromJson(res.get("paperImgs").toString(), List.class);
                    List<Map> apaperImgs1 = StringUtil.isEmpty(res.get("apaperImgs"))? new ArrayList<>() : GsonHelper.getInstance().fromJson(res.get("apaperImgs").toString(), List.class);
                    if (paperImgs!=null&&paperImgs.size() > 0) {
                        JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, paperImgs, res.get("paperPath").toString(), "", tppMap.get("backetName").toString());
                        if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                            res.put("updatePaperFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
                    }
                    if(paperImgs1.size() > 0 && apaperImgs1.size() > 0) {
                        JsonObject delFileObject = resourceService.deleteFiles(resourceUpload,apaperImgs1,paperImgs1,res.get("paperPath").toString(),tppMap.get("backetName").toString());
                        if (delFileObject.get("data").getAsJsonObject().get("delFiles") != null)
                            res.put("delPaperFiles",delFileObject.get("data").getAsJsonObject().get("delFiles").getAsJsonArray());
                    }
                    //身份证更新
                    List<MultipartFile> userSps = multipartRequest.getMultiFileMap().get("spFiles");
                    List<Map> userSps1 = StringUtil.isEmpty(res.get("userSps"))? new ArrayList<>() : GsonHelper.getInstance().fromJson(res.get("userSps").toString(), List.class);
                    List<Map> auserSps1 = StringUtil.isEmpty(res.get("auserSps"))? new ArrayList<>() : GsonHelper.getInstance().fromJson(res.get("auserSps").toString(), List.class);
                    if (userSps!=null&&userSps.size() > 0) {
                        JsonObject uploadFileObject = resourceService.uploadFiles(resourceUpload, userSps, res.get("userPath").toString(), "", tppMap.get("backetName").toString());
                        if (uploadFileObject.get("data").getAsJsonObject().get("files") != null)
                            res.put("updateSpFiles", uploadFileObject.get("data").getAsJsonObject().get("files").getAsJsonArray());
                    }
                    if(userSps1.size() > 0 && auserSps1.size() > 0) {
                        JsonObject delFileObject = resourceService.deleteFiles(resourceUpload,auserSps1,userSps1,res.get("userPath").toString(),tppMap.get("backetName").toString());
                        if (delFileObject.get("data").getAsJsonObject().get("delFiles") != null)
                            res.put("delSpFiles",delFileObject.get("data").getAsJsonObject().get("delFiles").getAsJsonArray());
                    }
                    //更新用户信息
                    if(!StringUtil.isEmpty(res.get("user"))) {
                        Map userMap = GsonHelper.getInstance().fromJson(res.get("user").toString(), Map.class);
                        userMap.put("updateTime", new Date());
                        userDao.updateUserInfo(userMap);
                    }
                    //更新店铺信息
                    shopMap.put("lastCreateTime",new Date());
                    if(!StringUtil.isEmpty(shopMap.get("foundingTime")) && shopMap.get("foundingTime").toString().indexOf("Z") != -1)
                        shopMap.put("foundingTime", DateTime.compareDate(shopMap.get("foundingTime").toString()));
                    if(!StringUtil.isEmpty(res.get("updateShopFiles"))) {
                        List<Map> updateShopFiles = GsonHelper.getInstance().fromJson(res.get("updateShopFiles").toString(), List.class);
                        shopMap.put("img", updateShopFiles.get(0).get("url"));
                    }
                    shopDao.updateShop(shopMap);
                    //更新证件信息
                    if(!StringUtil.isEmpty(res.get("paper"))) {
                        Map paperMap = GsonHelper.getInstance().fromJson(res.get("paper").toString(), Map.class);
                        paperMap.put("updateTime", new Date());
                        if(!StringUtil.isEmpty(paperMap.get("time"))) {
                            if(((List)paperMap.get("time")).get(0).toString().indexOf("Z") != -1 && ((List)paperMap.get("time")).get(1).toString().indexOf("Z") != -1) {
                                paperMap.put("startTime",DateTime.compareDate(((List)paperMap.get("time")).get(0).toString()));
                                paperMap.put("endTime",DateTime.compareDate(((List)paperMap.get("time")).get(1).toString()));
                            }
                        }
                        userDao.updateUsersPaper(paperMap);
                    }
                    //更新银行信息
                    if(!StringUtil.isEmpty(res.get("bank"))) {
                        Map bankMap = GsonHelper.getInstance().fromJson(res.get("bank").toString(), Map.class);
                        bankMap.put("updateTime", new Date());
                        userDao.updateUserBank(bankMap);
                    }
                    //证件资源操作
                    if(!StringUtil.isEmpty(res.get("delPaperFiles"))) {
                        List<Map> delPaperFiles = GsonHelper.getInstance().fromJson(res.get("delPaperFiles").toString(), List.class);
                        for(Map obj : delPaperFiles) {
                            resourceDao.deleteResource(obj);
                        }
                    }
                    if(!StringUtil.isEmpty(res.get("updatePaperFiles"))) {
                        List<Map> updatePaperFiles = GsonHelper.getInstance().fromJson(res.get("updatePaperFiles").toString(), List.class);
                        for(Map obj : updatePaperFiles) {
                            obj.put("type","ZP");
                            obj.put("refId",shopMap.get("userId"));
                            obj.put("createUserId", user.getUserId());
                            obj.put("lastCreateUserId", user.getUserId());
                            obj.put("createTime", new Date());
                            obj.put("lastCreateTime", new Date());
                            resourceDao.insertResourcet(obj);
                        }
                    }
                    //身份证操作
                    if(!StringUtil.isEmpty(res.get("delSpFiles"))) {
                        List<Map> delSpFiles = GsonHelper.getInstance().fromJson(res.get("delSpFiles").toString(), List.class);
                        for(Map obj : delSpFiles) {
                            resourceDao.deleteResource(obj);
                        }
                    }
                    if(!StringUtil.isEmpty(res.get("updateSpFiles"))) {
                        List<Map> updateSpFiles = GsonHelper.getInstance().fromJson(res.get("updateSpFiles").toString(), List.class);
                        for(Map obj : updateSpFiles) {
                            obj.put("type","SP");
                            obj.put("refId",shopMap.get("userId"));
                            obj.put("createUserId", user.getUserId());
                            obj.put("lastCreateUserId", user.getUserId());
                            obj.put("createTime", new Date());
                            obj.put("lastCreateTime", new Date());
                            resourceDao.insertResourcet(obj);
                        }
                    }
                }
                rtn.setCode(200);
                rtn.setMessage("success");
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteShop(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("id",jsonObject.get("id").getAsString());
        shopDao.deleteShop(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject querySettlement(JsonObject jsonObject) {
        jsonObject.addProperty("service","shop");
        return query(jsonObject,(map)->{
            return shopDao.selectSettlement(map);
        });
    }

    @Override
    public JsonObject updateSettlement(JsonObject jsonObject) {
        Rtn rtn = new Rtn("shop");
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            Map<String,Object> res = new HashMap<>();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("updateTime", new Date());
            res.put("userId", user.getUserId());
            shopDao.updateSettlement(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }

        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
