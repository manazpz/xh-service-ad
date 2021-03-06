package aq.dao.goods;

import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan("daoGoods")
public interface GoodsDao {

    //添加商品
    int insertGoods(Map<String,Object> map);

    //查询商品
    List<Map<String,Object>> selectGoods(Map<String,Object> map);

    //更新商品
    int updateGoods(Map<String,Object> map);

    //删除商品
    int deleteGoods(Map<String,Object> map);

    /**********************************************************************************/

    //添加置换车
    int insertReplacementCar(Map<String,Object> map);

    //查询置换车
    List<Map<String,Object>> selectReplacementCar(Map<String,Object> map);

    //更新置换车
    int updateReplacementCar(Map<String,Object> map);

    //删除置换车
    int deleteReplacementCar(Map<String,Object> map);

    /**********************************************************************************/

    //查询品牌
    List<Map<String,Object>> selectBrand(Map<String,Object> map);

    //添加品牌
    int insertBrand(Map<String,Object> map);

    //更新品牌
    int updateBrand(Map<String,Object> map);

    //删除品牌
    int deleteBrand(Map<String,Object> map);

    /**********************************************************************************/

    /**********************************************************************************/

    //查询品牌分类
    List<Map<String,Object>> selectBrandClass(Map<String,Object> map);

    //查询品牌分类关联表
    List<Map<String,Object>> selectBrandCorr(Map<String,Object> map);

    //添加品牌分类
    int insertBrandClass(Map<String,Object> map);

    //添加品牌分类对应表
    int insertBrandCorr(Map<String,Object> map);

    //删除品牌分类对应表
    int deleteBrandCorr(Map<String,Object> map);

    //更新品牌分类
    int updateBrandClass(Map<String,Object> map);

    //删除品牌分类
    int deleteBrandClass(Map<String,Object> map);

    /**********************************************************************************/

    //新增标签
    int instertLable(Map<String,Object> map);

    //查询标签
    List<Map<String,Object>> selectLable(Map<String,Object> map);

    /**********************************************************************************/

    //插入商品-标签对应表
    int insertGoodsLable(Map<String,Object> map);

    //查询商品-标签对应表
    List<Map<String,Object>> selectGoodsLable(Map<String,Object> map);

    //删除商品-标签对应表
    int deleteGoodsLable(Map<String,Object> map);



    /**********************************************************************************/

    //查询商品评论
    List<Map<String,Object>> selectGoodsComment(Map<String,Object> map);

    /**********************************************************************************/

    //插入首页商品
    int insertHomeGoods(Map<String,Object> map);

    //查询首页商品
    List<Map<String,Object>> selectHomeGoods(Map<String,Object> map);

    //更新首页商品
    int updateHomeGoods(Map<String,Object> map);



    /**********************************************************************************/


    //查询商品预估价格
    List<Map<String,Object>> selectForecastList(Map<String,Object> map);

    //新增商品预估价格
    int insertForecast(Map<String,Object> map);

    //删除商品预估价格
    int deleteForecast(Map<String,Object> map);

    //更新商品预估价格
    int updateForecast(Map<String,Object> map);

    //新增预测属性
    int insertForecastMain(Map<String,Object> map);

    //查询商品预测属性
    List<Map<String,Object>> selectForecastMainList(Map<String,Object> map);

    //删除商品预估价格属性
    int deleteForecastMain(Map<String,Object> map);

    //更新商品预估价格属性
    int updateForecastMain(Map<String,Object> map);


    /**********************************************************************************/

}
