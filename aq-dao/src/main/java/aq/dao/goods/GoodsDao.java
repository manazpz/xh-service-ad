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

    //查询商品
    int updateGoods(Map<String,Object> map);

    //查询商品
    int deleteGoods(Map<String,Object> map);

    //查询品牌
    List<Map<String,Object>> selectBrand(Map<String,Object> map);

    //添加置换车
    int insertReplacementCar(Map<String,Object> map);

    //查询置换车
    List<Map<String,Object>> selectReplacementCar(Map<String,Object> map);

    //更新置换车
    int updateReplacementCar(Map<String,Object> map);

    //删除置换车
    int deleteReplacementCar(Map<String,Object> map);

    //查询商品品牌
    List<Map<String,Object>> selectBrandList(Map<String,Object> map);

    //添加品牌
    int insertBrand(Map<String,Object> map);

    //更新品牌
    int updateBrand(Map<String,Object> map);

    //删除品牌
    int deleteBrand(Map<String,Object> map);

    //查询标签
    List<Map<String,Object>> selectLable(Map<String,Object> map);

}
