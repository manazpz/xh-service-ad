package aq.service.resource;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ResourceService extends BaseService {

    //单文件上传
    JsonObject uploadFile(HttpServletRequest request,HttpServletResponse response) throws IOException;

    //单文件删除
    JsonObject deleteFile(JsonObject jsonObject);

}
