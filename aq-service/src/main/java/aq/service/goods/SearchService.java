package aq.service.goods;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface SearchService extends BaseService {

    //搜索型号
    JsonObject querySearchSpec(JsonObject jsonObject);
}
