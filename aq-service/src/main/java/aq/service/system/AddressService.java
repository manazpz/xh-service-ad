package aq.service.system;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface AddressService extends BaseService {

    //新增地址
    JsonObject insertAddress(JsonObject jsonObject);

    //查询地址
    JsonObject queryAddress(JsonObject jsonObject);

    //删除地址
    JsonObject deleteAdress(JsonObject jsonObject);

    //更新地址
    JsonObject updateAdress(JsonObject jsonObject);


}
