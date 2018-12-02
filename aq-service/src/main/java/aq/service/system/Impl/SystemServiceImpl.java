package aq.service.system.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.oss.Oss;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.config.ConfigDao;
import aq.dao.shop.ShopDao;
import aq.dao.system.SystemDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.Func;
import aq.service.system.SystemService;
import com.google.gson.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceSystem")
@DyncDataSource
public class SystemServiceImpl extends BaseServiceImpl  implements SystemService {

    @Resource
    private SystemDao sysDao;

    @Resource
    private ShopDao shopDao;

    @Resource
    private ConfigDao configDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryLogin(JsonObject jsonObject){
        Map<String,Object> map = new HashMap<>();
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        if (jsonObject.isJsonObject()){
            String userName = jsonObject.get("username").getAsString().trim();
            String password = jsonObject.get("password").getAsString().trim();
            if (StringUtil.isEmpty(userName)||StringUtil.isEmpty(password)){
                rtn.setCode(498);
                rtn.setMessage("用户名或密码不允许为空！");
            }else {
                map.clear();
                map.put("userName",userName);
                List<Map<String,Object>> mapList = sysDao.selectSysLogin(map);
                if (mapList.size()<=0){
                    rtn.setCode(497);
                    rtn.setMessage("用户不存在！");
                }else {
                    if("SD".equals(mapList.get(0).get("STATUS"))) {
                        map.clear();
                        map.put("userId",mapList.get(0).get("ID"));
                        map.put("status","01");
                        List<Map<String, Object>> shops = shopDao.selectShop(map);
                        if(shops.size() < 1) {
                            rtn.setCode(404);
                            rtn.setMessage("验证未通过，等待审核！");
                            return Func.functionRtnToJsonObject.apply(rtn);
                        }
                    }
                    //验证密码
                    String encryptPwd = MD5.getMD5String(password),
                            okPwd = mapList.get(0).get("PASSWORD").toString();
                    if (encryptPwd.equals(okPwd)){
                        LocalDateTime localDateTime = DateTime.addTime(new Date(), ChronoUnit.HOURS, 1);
                        String token = UUIDUtil.getUUID();
                        String refreshToken = UUIDUtil.getUUID();
                        map.clear();
                        map.put("administratorId",mapList.get(0).get("ID"));
                        List<Map<String, Object>> tokenList = sysDao.selectSysToken(map);
                        map.clear();
                        map.put("exptime",localDateTime);
                        map.put("token",token);
                        map.put("refreshToken",refreshToken);
                        if(tokenList.size()>0){
                            map.put("administratorId",tokenList.get(0).get("administratorId"));
                            map.put("refresh",tokenList.get(0).get("refreshToken"));
                            sysDao.updateSytToken(map);
                        }else {
                            map.put("administratorId",mapList.get(0).get("ID"));
                            sysDao.insertSytToken(map);
                        }
                        rtn.setCode(200);
                        rtn.setMessage("验证通过！");
                        data = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(map)).getAsJsonObject();
                    }else {
                        rtn.setCode(496);
                        rtn.setMessage("密码错误！");
                    }
                }
            }
        }else {
            rtn.setCode(499);
            rtn.setMessage("参数格式不正确！");
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject refreshToken(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        LocalDateTime localDateTime = DateTime.addTime(new Date(), ChronoUnit.HOURS, 1);
        String token = UUIDUtil.getUUID();
        String refreshToken = UUIDUtil.getUUID();
        Map map = new HashMap();
        if(StringUtil.isEmpty(jsonObject.get("refreshToken"))) {
            rtn.setCode(50003);
            rtn.setMessage("刷新token不允许为空!");
        }else {
            map.clear();
            map.put("refresh",jsonObject.get("refreshToken").getAsString());
            List<Map> list = sysDao.selectSysToken(map);
            if(list.size()<=0){
                rtn.setCode(50002);
                rtn.setMessage("刷新token不符合!");
            }else {
                map.clear();
                map.put("administratorId",list.get(0).get("administratorId"));
                map.put("exptime",localDateTime);
                map.put("token",token);
                map.put("refreshToken",refreshToken);
                map.put("refresh",jsonObject.get("refreshToken").getAsString());
                sysDao.updateSytToken(map);
                data = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(map)).getAsJsonObject();
                rtn.setCode(200);
                rtn.setMessage("success");
            }
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryUserInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        List roles = new ArrayList();
        Map map = new HashMap();
        map.clear();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> users = sysDao.selectUserInfo(map);
        if(users.size() <= 0){
            rtn.setCode(60003);
            rtn.setMessage("当前登录者不存在!");
        }else {
            Map<String, Object> userMap = users.get(0);
            map.clear();
            map.put("administratorId",userMap.get("id"));
            List<Map> list = sysDao.selectSysPermissionUser(map);
            list.forEach(obj->{
                if(!StringUtil.isEmpty(obj.get("module"))) {
                    if("AM".equals(obj.get("module"))) {
                        roles.add(userMap.get("statusKey"));
                    }else {
                        roles.add(obj.get("module").toString());
                    }
                }
            });
            userMap.put("roles",roles);
            rtn.setCode(200);
            rtn.setMessage("success");
            data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(userMap)).getAsJsonObject();
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryToken(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        Map map = new HashMap();
        map.clear();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> tokens = sysDao.selectSysToken(map);
        if(tokens.size() <= 0){
            rtn.setCode(50002);
            rtn.setMessage("token不符合!");
        }else {
            if(isTokenExpire(tokens.get(0))) {
                rtn.setCode(50001);
                rtn.setMessage("token过期！");
            }else {
                rtn.setCode(200);
                rtn.setMessage("success");
                data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(tokens.get(0))).getAsJsonObject();
            }
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querySysPermissionUser(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        Map map = new HashMap();
        map.clear();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> permissions = sysDao.selectSysPermissionUser(map);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(permissions)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject querySysPermissionInfo(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        JsonObject data = new JsonObject();
        Map map = new HashMap();
        map.clear();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List<Map<String, Object>> permissions = sysDao.selectSysPermissionInfo(map);
        if(permissions.size() <= 0){
            rtn.setCode(40000);
            rtn.setMessage("无此权限!");
        }else {
            rtn.setCode(200);
            rtn.setMessage("success");
            data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(permissions.get(0))).getAsJsonObject();
        }
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    public Boolean isTokenExpire(Map map) {
        Date date1 = new Date();
        if(map.get("exptime") == null){
            return true;
        }
        Date date2 = (Date) map.get("exptime");
        Boolean aBoolean = DateTime.compareDate(date1, date2);
        return aBoolean;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject updatePassword(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map<String,Object> res = new HashMap<>();
        AbsAccessUser user = Factory.getContext().user();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            if(StringUtil.isEmpty(jsonObject.get("oldPwd")) || StringUtil.isEmpty(jsonObject.get("newPwd"))) {
                rtn.setCode(10001);
                rtn.setMessage("新旧密码不能为空！");
            }else {
                String oldPwd = MD5.getMD5String(jsonObject.get("oldPwd").getAsString().trim());
                String newPwd = MD5.getMD5String(jsonObject.get("newPwd").getAsString().trim());
                res.clear();
                res.put("id",user.getUserId());
                List<Map<String, Object>> lists = sysDao.selectPassword(res);
                if(lists.size() <= 0) {
                    rtn.setCode(10000);
                    rtn.setMessage("未登录！");
                }else {
                    String aPwd = ((Map)lists.get(0)).get("pwd").toString();
                    if(oldPwd.equals(aPwd)) {
                        res.put("updateTime",new Date());
                        res.put("password",newPwd);
                        sysDao.updateUser(res);
                        rtn.setCode(200);
                        rtn.setMessage("success");
                    }else {
                        rtn.setCode(10002);
                        rtn.setMessage("旧密码不符合，忘记请联系管理员重置！");
                    }
                }
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject resetPassword(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map<String,Object> res = new HashMap<>();
        if(StringUtil.isEmpty(jsonObject.get("id"))) {
            rtn.setCode(10003);
            rtn.setMessage("用户主键不为空！");
        }else {
            res.clear();
            res.put("id",jsonObject.get("id").getAsString().trim());
            res.put("updateTime",new Date());
            res.put("password",MD5.getMD5String("123456"));
            sysDao.updateUser(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject uploadImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Rtn rtn = new Rtn("System");
        AbsAccessUser user = Factory.getContext().user();
        JsonObject data = new JsonObject();
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
            List<MultipartFile> files = multiFileMap.get("avatar");
            Map<String,Object> res = new HashMap<>();
            res.clear();
            res.put("platform","OSS");
            List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
            if(tpp.size() < 1) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }else {
                if(files != null) {
                    Map<String, Object> tppMap = tpp.get(0);
                    ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(),tppMap.get("accessKeyId").toString(),tppMap.get("accessKeySecret").toString());
                    for (MultipartFile obj : files) {
                        Oss oss = resourceUpload.uploadFile(obj,"head",tppMap.get("towPath").toString(),user.getUserId(),tppMap.get("backetName").toString());
                        if("success".equals(oss.getCode())){
                            Map<String,Object> ress = new HashMap<>();
                            String fileurl = resourceUpload.getFileUrl(oss.getResult().get("FILEURL").toString(),tppMap.get("backetName").toString());
                            ress.clear();
                            ress.put("id",user.getUserId());
                            ress.put("headPortrait",fileurl);
                            data.addProperty("headPortrait",fileurl);
                            sysDao.updateUser(ress);
                        }else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                    }
                }
                rtn.setCode(200);
                rtn.setMessage("success");
                rtn.setData(data);
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject uploadSwitch(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map<String,Object> res = new HashMap<>();
        if(StringUtil.isEmpty(jsonObject.get("id"))) {
            rtn.setCode(10003);
            rtn.setMessage("主键不为空！");
        }else {
            res.clear();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            res.put("updateTime",new Date());
            sysDao.updateSwitch(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject querySwitch(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map map = new HashMap();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        map.clear();
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        List list = sysDao.selectSwitch(map);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryCustomService(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map map = new HashMap();
        List list = sysDao.selectCustomService(map);
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject querySmsList(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map map = new HashMap();
        List list = sysDao.selectSms(map);
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        data.add("items",jsonArray);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject updateSms(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map<String,Object> res = new HashMap<>();
        if(StringUtil.isEmpty(jsonObject.get("id"))) {
            rtn.setCode(10003);
            rtn.setMessage("主键不为空！");
        }else {
            res.clear();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            sysDao.updateSms(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertSms(JsonObject jsonObject) {
        Rtn rtn = new Rtn("System");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("id",UUIDUtil.getUUID());
        res.put("def","N");
        sysDao.insertSms(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
