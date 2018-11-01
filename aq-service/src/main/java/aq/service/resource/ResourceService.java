package aq.service.resource;

import aq.common.util.ResourceUpload;
import aq.service.base.BaseService;
import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ResourceService extends BaseService {

    //单文件上传
    JsonObject uploadFile(HttpServletRequest request,HttpServletResponse response) throws IOException;

    //单文件删除
    JsonObject deleteFile(JsonObject jsonObject);

    //多文件上传（暂无线程控制，后期修改）
    JsonObject uploadFiles(ResourceUpload resourceUpload, List<MultipartFile> files, String path, String id, String backetName) throws IOException;

    //多文件删除
    JsonObject deleteFiles(ResourceUpload resourceUpload, List<Map> afileList, List<Map> fileList, String path, String backetName);

    //查询资源
    JsonObject queryaResource(JsonObject jsonObject);

    //新增资源
    JsonObject insertResource(JsonObject jsonObject);

    //更新资源
    JsonObject updateResource(JsonObject jsonObject);

    //删除资源
    JsonObject deleteResource(JsonObject jsonObject);
}
