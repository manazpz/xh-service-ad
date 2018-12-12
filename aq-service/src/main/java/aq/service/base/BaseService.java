package aq.service.base;


import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Service层基类
 * 重要说明：Service层的方法如果是提供给Controller层调用的话，建议方法的参数类型只有JsonObject和Map两种
 * 当Controller层的Rest接口的访问方式为HttpGet方式时，采用Map类型，其他方式建议为JsonObject类型
 * Created by ywb on 2017-02-23.
 */
public interface BaseService {

    //查询
    JsonObject query(JsonObject jsonObject, Function<Map<String,Object>,List<Map<String,Object>>> func);

    //不分页查询
    JsonObject queryNoPage(JsonObject jsonObject, Function<Map<String,Object>,List<Map<String,Object>>> func);

}
