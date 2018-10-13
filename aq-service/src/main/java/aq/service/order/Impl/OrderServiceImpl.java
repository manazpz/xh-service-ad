package aq.service.order.Impl;

import aq.common.annotation.DyncDataSource;
import aq.common.other.Rtn;
import aq.common.util.*;
import aq.dao.order.OrderDao;
import aq.dao.user.UserDao;
import aq.service.base.Impl.BaseServiceImpl;
import aq.service.order.OrderService;
import aq.service.system.Func;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ywb on 2017-02-23.
 */
@Service("serviceOrder")
@DyncDataSource
public class OrderServiceImpl extends BaseServiceImpl  implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private UserDao userDao;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public JsonObject queryorderList(JsonObject jsonObject) {
        jsonObject.addProperty("service","order");
        return query(jsonObject,(map)->{
            return orderDao.selectorderList(map);
        });
    }

}
