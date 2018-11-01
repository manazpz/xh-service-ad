package aq.service.jobs;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface JobsService extends BaseService {

    //获取验机数据
    JsonObject queryOrderCollect(JsonObject jsonObject);

}
