package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface GoodsService extends BaseService {

    //新增商品分类
    JsonObject insertGoodsClassify(JsonObject jsonObject);

    //查询商品分类
    JsonObject queryclassify(JsonObject jsonObject);

    //查询商品品牌分类
    JsonObject querybrandList(JsonObject jsonObject);

    //新增商品品牌
    JsonObject insertBrand(JsonObject jsonObject);

    //更新商品品牌
    JsonObject updateBrand(JsonObject jsonObject);

    //更新商品品牌
    JsonObject deleteBrand(JsonObject jsonObject);

    //更新商品品牌
    JsonObject queryspec(JsonObject jsonObject);

    //新增商品规格
    JsonObject insertSpec(JsonObject jsonObject);

    //更新商品规格
    JsonObject updateSpec(JsonObject jsonObject);

    //更新商品规格
    JsonObject deleteSpec(JsonObject jsonObject);

    //更新商品规格值
    JsonObject insertSpecValue(JsonObject jsonObject);

    //查询规格值列表
    JsonObject queryspecValue(JsonObject jsonObject);


}
