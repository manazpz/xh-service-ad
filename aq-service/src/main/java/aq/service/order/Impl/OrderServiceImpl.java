package aq.service.order.Impl;

import aq.common.annotation.DyncDataSource;
import aq.dao.order.OrderDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceOrder")
@DyncDataSource
public class OrderServiceImpl extends BaseServiceImpl  implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        jsonObject.addProperty("service","order");
        return query(jsonObject,(map)->{
            return orderDao.selectorderList(map);
        });
    }
}
