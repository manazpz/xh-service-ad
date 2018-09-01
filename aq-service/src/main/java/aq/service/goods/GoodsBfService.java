package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface GoodsBfService extends BaseService {

    //查询商品
    JsonObject queryGoods(JsonObject jsonObject);

    //更新商品
    JsonObject updateGoods(JsonObject jsonObject);

    //删除商品
    JsonObject deleteGoods(JsonObject jsonObject);

    //更新库存
    JsonObject updateStock(JsonObject jsonObject);

    //查询分类，返回级联
    JsonObject selectClassifyCascade(JsonObject jsonObject);

    //查询分类规格
    JsonObject selectClassifySpec(JsonObject jsonObject);

    //查询分类规格参数
    JsonObject selectClassifySpecParam(JsonObject jsonObject);

    //查询品牌
    JsonObject selectBrand(JsonObject jsonObject);

}
