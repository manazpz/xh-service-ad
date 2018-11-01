package aq.service.resource.Impl;

import aq.common.access.AbsAccessUser;
import aq.common.access.Factory;
import aq.common.annotation.DyncDataSource;
import aq.common.oss.Oss;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.ResourceUpload;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.config.ConfigDao;
import aq.dao.resource.ResourceDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.resource.ResourceService;
import aq.service.system.Func;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceResource")
@DyncDataSource
public class ResourceServiceImpl extends BaseServiceImpl  implements ResourceService {

    @Resource
    private ConfigDao configDao;

    @Resource
    private ResourceDao resourceDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Rtn rtn = new Rtn("Resource");
        JsonObject data = new JsonObject();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        Map<String,Object> res = new HashMap<>();
        res.clear();
        res.put("platform","OSS");
        List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
        if(tpp.size() < 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }else {
            Map<String, Object> tppMap = tpp.get(0);
            ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(),tppMap.get("accessKeyId").toString(),tppMap.get("accessKeySecret").toString());
            String uuid = UUIDUtil.getUUID();
            Oss oss = resourceUpload.uploadFile(file,request.getParameter("path"),uuid,tppMap.get("backetName").toString());
            if("success".equals(oss.getCode())){
                String fileurl = resourceUpload.getFileUrl(oss.getResult().get("FILEURL").toString(),tppMap.get("backetName").toString());
                res.clear();
                String[] split = file.getOriginalFilename().split("\\.");
                res.put("id",uuid);
                res.put("name", StringUtil.isEmpty(split[0])?"":split[0]);
                res.put("url",fileurl);
                res.put("extend",StringUtil.isEmpty(split[1])?"":split[1]);
                res.put("link",StringUtil.isEmpty(request.getParameter("link"))?"":request.getParameter("link"));
                res.put("size",file.getSize());
                rtn.setCode(200);
                rtn.setMessage("success");
                data.add("file",GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(res)).getAsJsonObject());
                rtn.setData(data);
            }else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteFile(JsonObject jsonObject){
        Rtn rtn = new Rtn("Resource");
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
            resourceUpload.deleteFileToOSS(tppMap.get("backetName").toString(),jsonObject.get("path").getAsString(),jsonObject.get("id").getAsString()+"."+jsonObject.get("extend").getAsString());
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject uploadFiles(ResourceUpload resourceUpload, List<MultipartFile> files, String path, String id, String backetName) throws IOException {
        Rtn rtn = new Rtn("Resource");
        JsonObject data = new JsonObject();
        List fileData = new ArrayList();
        if(files != null) {
            for (MultipartFile obj : files) {
                String uuid = StringUtil.isEmpty(id)? UUIDUtil.getUUID() : id;
                Oss oss = resourceUpload.uploadFile(obj,path,uuid,backetName.toString());
                if("success".equals(oss.getCode())){
                    Map<String,Object> ress = new HashMap<>();
                    String fileurl = resourceUpload.getFileUrl(oss.getResult().get("FILEURL").toString(),backetName);
                    ress.clear();
                    String[] split = obj.getOriginalFilename().split("\\.");
                    ress.put("id",uuid);
                    ress.put("name", StringUtil.isEmpty(split[0])?"":split[0]);
                    ress.put("url",fileurl);
                    ress.put("extend",StringUtil.isEmpty(split[1])?"":split[1]);
                    ress.put("size",obj.getSize());
                    fileData.add(ress);
                }else {
                    return null;
                }
            }
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("files", GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(fileData)).getAsJsonArray());
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject deleteFiles(ResourceUpload resourceUpload,List<Map> afileList,List<Map> fileList,String path, String backetName) {
        Rtn rtn = new Rtn("Resource");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        List<Map> ress = new ArrayList<>();
        if (fileList.size() > 0) {
            for (Map obj1 : afileList) {
                Boolean flag = true;
                for (Map obj2 : fileList) {
                    if(obj1.get("id").equals(obj2.get("id"))) {
                        flag = false;
                    }
                }
                if (flag) {
                    ress.add(obj1);
                }
            }
        }
        for (Map obj : ress) {
            resourceUpload.deleteFileToOSS(backetName,path,obj.get("id")+"."+obj.get("extend"));
        }
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("delFiles", GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(ress)).getAsJsonArray());
        rtn.setData(data);
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject queryaResource(JsonObject jsonObject) {
        jsonObject.addProperty("service","Resource");
        return query(jsonObject,(map)->{
            return resourceDao.selectResource(map);
        });
    }

    @Override
    public JsonObject insertResource(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Resource");
        AbsAccessUser user = Factory.getContext().user();
        Map<String,Object> res = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            if (res.get("files") instanceof ArrayList) {
                List<Map> files = (List<Map>) res.get("files");
                for (int i=0;i<files.size();i++) {
                    files.get(i).put("type",res.get("type"));
                    files.get(i).put("name",res.get("name"));
                    files.get(i).put("refId",StringUtil.isEmpty(res.get("refId"))?"":res.get("refId"));
                    files.get(i).put("link",StringUtil.isEmpty(res.get("link"))?"":res.get("link"));
                    files.get(i).put("createUserId", user.getUserId());
                    files.get(i).put("lastCreateUserId", user.getUserId());
                    files.get(i).put("createTime", new Date());
                    files.get(i).put("lastCreateTime", new Date());
                    resourceDao.insertResourcet(files.get(i));
                }
            }
            rtn.setCode(200);
            rtn.setMessage("success");
        }

        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Override
    public JsonObject updateResource(JsonObject jsonObject) {
        AbsAccessUser user = Factory.getContext().user();
        Rtn rtn = new Rtn("Resource");
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> ress = new HashMap<>();
        res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (user == null) {
            rtn.setCode(10000);
            rtn.setMessage("未登录！");
        }else {
            if(res.get("files") instanceof List) {
                Map respurce = (Map) ((List) res.get("files")).get(0);
                ress.clear();
                ress.put("id",respurce.get("id"));
                ress.put("type",res.get("type"));
                List<Map<String, Object>> respurces1 = resourceDao.selectResource(ress);
                if(respurces1.size() >0) {
                    ress.put("id",respurces1.get(0).get("id"));
                    resourceDao.deleteResource(ress);
                    ress.clear();
                    ress.put("id",respurce.get("id"));
                    ress.put("name", res.get("name").toString());
                    ress.put("url",respurce.get("url"));
                    ress.put("extend",respurce.get("extend"));
                    ress.put("size",respurce.get("size"));
                    ress.put("type",res.get("type").toString());
                    ress.put("link",res.get("link").toString());
//                    ress.put("refId",StringUtil.isEmpty(res.get("id"))?"":res.get("id").toString());
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
    public JsonObject deleteResource(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Resource");
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
            resourceUpload.deleteFileToOSS(tppMap.get("backetName").toString(),jsonObject.get("path").getAsString(),jsonObject.get("id").getAsString()+"."+jsonObject.get("extend").getAsString());
            res.clear();
            res.put("id", jsonObject.get("id").getAsString());
            resourceDao.deleteResource(res);
            rtn.setCode(200);
            rtn.setMessage("success");
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

}
