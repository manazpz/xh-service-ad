package aq.service.resource;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ResourceApiService extends BaseService {

    //查询资源
    JsonObject queryaResource(JsonObject jsonObject);

    //多文件上传（暂无线程控制，后期修改）
    JsonObject uploadFiles(HttpServletRequest request, HttpServletResponse response) throws IOException;

    //删除资源
    JsonObject deleteResource(JsonObject jsonObject);

}
