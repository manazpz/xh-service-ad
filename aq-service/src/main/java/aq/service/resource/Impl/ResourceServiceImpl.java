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
import aq.service.resource.ResourceService;
import aq.service.system.Func;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceResource")
@DyncDataSource
public class ResourceServiceImpl extends BaseServiceImpl  implements ResourceService {

    @Resource
    private ConfigDao configDao;

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


}
