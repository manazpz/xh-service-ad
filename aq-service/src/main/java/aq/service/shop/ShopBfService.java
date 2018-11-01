package aq.service.shop;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ShopBfService extends BaseService {

    //查询店铺
    JsonObject queryShop(JsonObject jsonObject);

    //更新商品
    JsonObject updateShop(JsonObject jsonObject);

    //查询店铺
    JsonObject querySettlement(JsonObject jsonObject);

}
