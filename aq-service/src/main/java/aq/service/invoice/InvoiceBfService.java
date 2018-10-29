package aq.service.invoice;

import aq.service.base.BaseService;
import com.google.gson.JsonObject;

/**
 * Created by ywb on 2017-02-23.
 */
public interface InvoiceBfService extends BaseService {

    //插入发票
    JsonObject insertInvoice(JsonObject jsonObject);

    //更新发票
    JsonObject updateInvoice(JsonObject jsonObject);

    //查询发票
    JsonObject selectInvoice(JsonObject jsonObject);

}
