package aq.service.news;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface NewsBfService extends BaseService {

    //查询订单日志列表
    JsonObject queryCommentList(JsonObject jsonObject);

    //回复评论
    JsonObject insertComment(JsonObject jsonObject);
}
