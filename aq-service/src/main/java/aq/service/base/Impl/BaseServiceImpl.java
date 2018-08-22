package aq.service.base.Impl;

import aq.common.other.Rtn;
import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.common.util.StringUtil;
import aq.service.base.BaseService;
import aq.service.system.Func;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class BaseServiceImpl implements BaseService {

    @Override
    public JsonObject query(JsonObject jsonObject, Function<Map<String,Object>, List<Map<String, Object>>> func) {
        Rtn rtn = new Rtn(jsonObject.get("service").getAsString());
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        data.addProperty("total",0);
        data.add("items",jsonArray);
        Map<String,Object> map = new HashMap<>();
        Integer pageNum = StringUtil.isEmpty(jsonObject.get("pageNum"))?1:Integer.parseInt(jsonObject.get("pageNum").getAsString());
        Integer pageSize = jsonObject.get("pageSize") == null ? 1 : Integer.parseInt(jsonObject.get("pageSize").getAsString());
        PageHelper.startPage(pageNum,pageSize);
        map = GsonHelper.getInstance().fromJson(jsonObject,Map.class);
        if (map.get("list")!=null){
            List list = new ArrayList();
            Map<String,Object> mapList = new HashMap<>();
            mapList = (Map)map.get("list");
            for (String key : mapList.keySet()){
                list = Arrays.asList(mapList.get(key).toString().split(","));
                map.put(key,list);
            }
        }
        if (map.get("map")!=null){
            Map<String,Object> _mapList = new HashMap<>();
            _mapList = (Map)map.get("map");
            for (String _key : _mapList.keySet()){
                map.put(_key,_mapList.get(_key));
            }
        }
        PageInfo pageInfo = new PageInfo(func.apply(map));
        rtn.setCode(200);
        rtn.setMessage("success");
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(pageInfo.getList())).getAsJsonArray();
        data.addProperty("total",pageInfo.getTotal());
        data.addProperty("pageNum",pageNum);
        data.addProperty("pageSize",pageSize);
        data.addProperty("paginatorId",StringUtil.isEmpty(jsonObject.get("paginatorId"))?"":jsonObject.get("paginatorId").getAsString());
        data.add("items",jsonArray);
        rtn.setFrom(jsonObject.get("from")==null?"":jsonObject.get("from").getAsString());
        rtn.setData(data);
        return  Func.functionRtnToJsonObject.apply(rtn);
    }
}
