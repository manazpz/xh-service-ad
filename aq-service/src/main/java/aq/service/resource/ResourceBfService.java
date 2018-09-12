package aq.service.resource;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ResourceBfService extends BaseService {

    //多文件上传（暂无线程控制，后期修改）
    JsonObject uploadFiles(HttpServletRequest request,HttpServletResponse response) throws IOException;

    //
    //多文件删除
    JsonObject deleteFiles(JsonObject jsonObject);
}
