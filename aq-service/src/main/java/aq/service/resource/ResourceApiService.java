package aq.service.resource;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ResourceApiService extends BaseService {

    //查询资源
    JsonObject queryaResource(JsonObject jsonObject);

}
