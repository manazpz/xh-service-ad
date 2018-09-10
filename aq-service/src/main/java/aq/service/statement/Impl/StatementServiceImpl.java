package aq.service.statement.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.config.ConfigDao;
import aq.dao.resource.ResourceDao;
import aq.dao.statement.StatementDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.statement.StatementService;
import aq.service.system.Func;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceStatement")
@DyncDataSource
public class StatementServiceImpl extends BaseServiceImpl implements StatementService {

    @Resource
    private StatementDao statementDao;

    @Resource
    private ResourceDao resourceDao;

    @Resource
    private ConfigDao configDao;

    @Override
    public JsonObject insertStatement(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Statement");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            String id = UUIDUtil.getUUID();
            res.put("id", id);
            res.put("createUserId", user.getUserId());
            res.put("createTime", new Date());
            res.put("updateTime", new Date());
            statementDao.insertStatement(res);
            if(res.get("files") instanceof List) {
                Map respurce = (Map) ((List) res.get("files")).get(0);
                ress.clear();
                ress.put("id",respurce.get("id"));
                ress.put("name", respurce.get("name"));
                ress.put("url",respurce.get("url"));
                ress.put("extend",respurce.get("extend"));
                ress.put("size",respurce.get("size"));
                ress.put("type",res.get("type").toString());
                ress.put("refId",id);
                ress.put("createUserId", user.getUserId());
                ress.put("lastCreateUserId", user.getUserId());
                ress.put("createTime", new Date());
                ress.put("lastCreateTime", new Date());
                resourceDao.insertResourcet(ress);
            }
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject selectStatementList(JsonObject jsonObject) {
        jsonObject.addProperty("service","Statement");
        return query(jsonObject,(map)->{
            return statementDao.selectStatement(map);
        });
    }

    @Override
    public JsonObject updateStatement(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Statement");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            res.put("createUserId", user.getUserId());
            res.put("updateTime", new Date());
            statementDao.updateStatement(res);
            if(res.get("files") instanceof List) {
                Map respurce = (Map) ((List) res.get("files")).get(0);
                ress.clear();
                ress.put("id",respurce.get("id"));
                ress.put("type",res.get("type"));
                ress.put("refId",res.get("id"));
                List<Map<String, Object>> respurces1 = resourceDao.selectResource(ress);
                if(respurces1.size() < 1) {
                    ress.clear();
                    ress.put("refId",res.get("id"));
                    ress.put("type",res.get("type"));
                    List<Map<String, Object>> respurces2 = resourceDao.selectResource(ress);
                    if(respurces2.size() > 0) {
                        Map<String, Object> respurce2 = respurces2.get(0);
                        ress.clear();
                        ress.put("id",respurce2.get("id"));
                        resourceDao.deleteResource(ress);
                    }
                    ress.clear();
                    ress.put("id",respurce.get("id"));
                    ress.put("name", respurce.get("name"));
                    ress.put("url",respurce.get("url"));
                    ress.put("extend",respurce.get("extend"));
                    ress.put("size",respurce.get("size"));
                    ress.put("type",res.get("type").toString());
                    ress.put("refId",res.get("id").toString());
                    ress.put("createUserId", user.getUserId());
                    ress.put("lastCreateUserId", user.getUserId());
                    ress.put("createTime", new Date());
                    ress.put("lastCreateTime", new Date());
                    resourceDao.insertResourcet(ress);
                }
            }
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteStatement(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Statement");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("platform","OSS");
        List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
        if(tpp.size() < 1) {
            rtn.setCode(404);
            rtn.setMessage("远程仓库链接出错！");
        }else {
            Map<String, Object> tppMap = tpp.get(0);
            ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(),tppMap.get("accessKeyId").toString(),tppMap.get("accessKeySecret").toString());
            resourceUpload.deleteFileToOSS(tppMap.get("backetName").toString(),jsonObject.get("path").getAsString(),jsonObject.get("imgId").getAsString()+"."+jsonObject.get("imgExtend").getAsString());
            res.clear();
            res.put("id", jsonObject.get("id").getAsString());
            statementDao.deleteStatement(res);
            res.clear();
            res.put("id", jsonObject.get("imgId").getAsString());
            resourceDao.deleteResource(res);
        }
        return null;
    }
}
