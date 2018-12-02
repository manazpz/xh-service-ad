package aq.service.resource.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.oss.Oss;
import aq.common.other.Rtn;
import aq.common.util.GsonHelper;
import aq.common.util.ResourceUpload;
import aq.common.util.StringUtil;
import aq.common.util.UUIDUtil;
import aq.dao.config.ConfigDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.resource.ResourceBfService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceBfResource")
@DyncDataSource
public class ResourceBfServiceImpl extends BaseServiceImpl  implements ResourceBfService {

    @Resource
    private ConfigDao configDao;


    @Override
    public JsonObject uploadFiles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Rtn rtn = new Rtn("Resource");
        JsonObject data = new JsonObject();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.get("files");
        List fileData = new ArrayList();
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
                    String uuid = StringUtil.isEmpty(request.getParameter("id"))? UUIDUtil.getUUID() : request.getParameter("id");
                    Oss oss = resourceUpload.uploadFile(obj,request.getParameter("path"),tppMap.get("towPath").toString(),uuid,tppMap.get("backetName").toString());
                    if("success".equals(oss.getCode())){
                        Map<String,Object> ress = new HashMap<>();
                        String fileurl = resourceUpload.getFileUrl(oss.getResult().get("FILEURL").toString(),tppMap.get("backetName").toString());
                        ress.clear();
                        String[] split = obj.getOriginalFilename().split("\\.");
                        ress.put("id",uuid);
                        ress.put("name", StringUtil.isEmpty(split[0])?"":split[0]);
                        ress.put("url",fileurl);
                        ress.put("extend",StringUtil.isEmpty(split[1])?"":split[1]);
                        ress.put("size",obj.getSize());
                        fileData.add(ress);
                    }else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                }
            }
            rtn.setCode(200);
            rtn.setMessage("success");
            data.add("files", GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(fileData)).getAsJsonArray());
            rtn.setData(data);
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject deleteFiles(JsonObject jsonObject) {
        Rtn rtn = new Rtn("Resource");
        Map<String,Object> res = new HashMap<>();
        JsonObject data = new JsonObject();
        List<Map> ress = new ArrayList<>();
        res.clear();
        res.put("platform","OSS");
        List<Map<String, Object>> tpp = configDao.selectTppConfig(res);
        if(tpp.size() < 1) {
            rtn.setCode(404);
            rtn.setMessage("远程仓库链接出错！");
        }else {
            res.clear();
            res = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
            List<Map> afileList = new ArrayList();
            List<Map> fileList = new ArrayList();
            if (!StringUtil.isEmpty(res.get("afileList"))) {
                afileList.addAll(GsonHelper.getInstance().fromJson(res.get("afileList").toString(),List.class));
            }
            if (!StringUtil.isEmpty(res.get("fileList"))) {
                fileList.addAll(GsonHelper.getInstance().fromJson(res.get("fileList").toString(),List.class));
            }
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
            Map<String, Object> tppMap = tpp.get(0);
            ResourceUpload resourceUpload = new ResourceUpload(tppMap.get("endpoint").toString(),tppMap.get("accessKeyId").toString(),tppMap.get("accessKeySecret").toString());
            for (Map obj : ress) {
                resourceUpload.deleteFileToOSS(tppMap.get("backetName").toString(),jsonObject.get("path").getAsString(),tppMap.get("towPath").toString(),obj.get("id")+"."+obj.get("extend"));
            }
            rtn.setCode(200);
            rtn.setMessage("success");
            data.add("delFiles", GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(ress)).getAsJsonArray());
            rtn.setData(data);
        }
        return Func.functionRtnToJsonObject.apply(rtn);
    }
}
