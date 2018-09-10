package aq.service.statement;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface StatementApiService extends BaseService {

    //查询声明列表
    JsonObject selectStatementList(JsonObject jsonObject);

}
