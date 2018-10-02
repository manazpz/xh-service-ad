package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface GoodsApiService extends BaseService {

    //查询商品
    JsonObject queryGoods(JsonObject jsonObject);

    //查询热门商品
    JsonObject queryLableGoods(JsonObject jsonObject);

    //查询商品选择
    JsonObject queryChoices(JsonObject jsonObject);

    //获取商品规格列表
    JsonObject querygoodsSpecs(JsonObject jsonObject);

    //商品估价
    JsonObject evaluates(JsonObject jsonObject);

    //添加置换车
    JsonObject insertReplacementCar(JsonObject jsonObject);

    //更新置换车
    JsonObject updateReplacementCar(JsonObject jsonObject);

    //查询置换车
    JsonObject queryReplacementCar(JsonObject jsonObject);

    //删除置换车
    JsonObject deleteReplacementCar(JsonObject jsonObject);

    //查询回收方式
    JsonObject recoveryList(JsonObject jsonObject);

    //查询回收方式
    JsonObject queryrecoveryList(JsonObject jsonObject);

    //新增回收方式
    JsonObject insertrecoveryListUser(JsonObject jsonObject);

}
