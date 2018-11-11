package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ywb on 2017-02-23.
 */
public interface UserApiService extends BaseService {

    //新增用户
    JsonObject queryUserInfos(JsonObject jsonObject);

    //新增用户
    JsonObject updateUserInfos(JsonObject jsonObject);

    //发送验证码
    JsonObject sendCode(JsonObject jsonObject);

    //新增意见反馈
    JsonObject insertSuggestion(JsonObject jsonObject);

}
