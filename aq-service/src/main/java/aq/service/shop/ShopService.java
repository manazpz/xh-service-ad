package aq.service.shop;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ywb on 2017-02-23.
 */
public interface ShopService extends BaseService {

    //插入店铺
    JsonObject insertShop(HttpServletRequest request,JsonObject jsonObject) throws Exception;

    //查询店铺
    JsonObject queryShop(JsonObject jsonObject);

    //查询店铺
    JsonObject queryShopDetail(JsonObject jsonObject);

    //更新商品
    JsonObject updateShop(HttpServletRequest request,JsonObject jsonObject) throws Exception;

    //删除商品
    JsonObject deleteShop(JsonObject jsonObject);

    //查询结算
    JsonObject querySettlement(JsonObject jsonObject);

    //更新结算
    JsonObject updateSettlement(JsonObject jsonObject);
}
