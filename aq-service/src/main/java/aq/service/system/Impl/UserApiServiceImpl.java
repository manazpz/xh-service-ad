package aq.service.system.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.system.SystemDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.system.Func;
import aq.service.system.UserApiService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceUserApi")
@DyncDataSource
public class UserApiServiceImpl extends BaseServiceImpl implements UserApiService {

    @Resource
    private UserDao userDao;

    @Resource
    private SystemDao systemDao;

    @Override
    public JsonObject queryUserInfos(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        res.clear();
        res.put("openid",jsonObject.get("openId").getAsString());
        List<Map<String, Object>> list = userDao.selectUserInfos(res);
        if(list.size()>0) {
            res.clear();
            res.put("userId",list.get(0).get("id"));
            List roles = userDao.selectUserRoleFlag(res);
            list.get(0).put("roles",roles);
            jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(list)).getAsJsonArray();
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject updateUserInfos(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        res.put("openid",jsonObject.get("openId").getAsString());
        res.put("phone",jsonObject.get("phone").getAsString());
        userDao.updateUserInfos(res);
        rtn.setCode(200);
        rtn.setMessage("success");
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject sendCode(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        Map result = new HashMap();
        res.put("def","Y");
        List<Map<String, Object>> maps = systemDao.selectSms(res);
        if(maps.size()> 0 ){
            String url = maps.get(0).get("url").toString();
            String username = maps.get(0).get("username").toString();
            String password = maps.get(0).get("password").toString();
            String mobile = jsonObject.get("phone").getAsString();
            String productid = maps.get(0).get("productid").toString();
            String xh ="";
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);//生成6位短信验证码
            String content ="此次登录验证码"+verifyCode + "【小换】";//内容
            String tkey = DateTime.getNowTime("yyyyMMddHHmmss");
            try{
                content= URLEncoder.encode(content,"utf-8");
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String param="url="+url+"&username="+username+"&password="+MD5.getMD5String(MD5.getMD5String(password)+tkey)+"&tkey="+tkey+"&mobile="+mobile+"&content="+content+"&productid="+productid+"&xh"+xh;
            String ret=HttpRequest.sendPost(url, param);//sendPost or sendGet  即get和post方式
            if("1".equals(ret.split(",")[0])){
                rtn.setCode(200);
                result.put("code",verifyCode);
                data =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(result)).getAsJsonObject();
                rtn.setMessage("success");
                rtn.setData(data);
            }else{
                rtn.setCode(404);
                rtn.setMessage("error");
            }

        }else{
            rtn.setCode(404);
            rtn.setMessage("error");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject insertSuggestion(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("openid",res.get("openId"));
        List<Map<String, Object>> maps = userDao.selectUserInfos(rest);
        if(maps.size()>0){
            rest.clear();
            rest.put("id",UUIDUtil.getUUID());
            rest.put("user_id",maps.get(0).get("id"));
            rest.put("content",res.get("content"));
            rest.put("createTime",new Date());
            userDao.insertSuggestion(rest);
            rtn.setCode(200);
            rtn.setMessage("success");
        }else{
            rtn.setCode(500);
            rtn.setMessage("error");
        }

        return  Func.functionRtnToJsonObject.apply(rtn);
    }


    @Override
    public JsonObject queryUserBank(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        JsonObject json = new JsonObject();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("openid",res.get("openId"));
        List<Map<String, Object>> userinfo = userDao.selectUserInfos(rest);
        if(userinfo.size()>0){
            rest.clear();
            rest.put("user_id",userinfo.get(0).get("id"));
            List<Map<String, Object>> maps = userDao.selectUserBank(rest);
            if(maps.size()>0){
                json = GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(maps.get(0))).getAsJsonObject();
                rtn.setCode(200);
                rtn.setData(json);
                rtn.setMessage("success");
            }else{
                rtn.setCode(499);
                rtn.setMessage("银行信息不存在不存在！");
            }
        }else{
            rtn.setCode(500);
            rtn.setMessage("用户不存在！");
        }
        return  Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject insertBank(JsonObject jsonObject) {
        Rtn rtn = new Rtn("User");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        rest.put("openid",res.get("openId"));
        List<Map<String, Object>> maps = userDao.selectUserInfos(rest);
        if(maps.size()>0){
            rest.clear();
            rest.put("basicId",maps.get(0).get("id"));
            rest.put("bankName",res.get("bank_name"));
            rest.put("bankAddress",res.get("bank_adress"));
            rest.put("accountName",res.get("account_name"));
            rest.put("accountNo",res.get("account_no"));
            rest.put("createTime",new Date());
            if("update".equals(res.get("type"))){
                rest.put("id",res.get("id"));
                userDao.updateUserBank(rest);
            }else{
                rest.put("id",UUIDUtil.getUUID());
                userDao.insertUsersBank(rest);
            }
            rtn.setCode(200);
            rtn.setMessage("success");
        }else{
            rtn.setCode(500);
            rtn.setMessage("error");
        }

        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
