package aq.service.statement;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface StatementService extends BaseService {

    //插入声明
    JsonObject insertStatement(JsonObject jsonObject);

    //查询声明列表
    JsonObject selectStatementList(JsonObject jsonObject);

    //更新声明
    JsonObject updateStatement(JsonObject jsonObject);

    //插入声明
    JsonObject deleteStatement(JsonObject jsonObject);

}
