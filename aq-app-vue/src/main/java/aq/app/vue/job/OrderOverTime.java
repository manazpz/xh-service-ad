package aq.app.vue.job;

import aq.common.util.DateTime;
import aq.service.jobs.JobsService;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.logging.Logger;


/**
 * Created by admin on 2018/10/30.
 */
public class OrderOverTime {

    @Resource
    private JobsService jobsService;

    public static Boolean flag = true;
    public static Logger logger = Logger.getLogger(OrderOverTime.class.getName());
    //每天晚上12点定时运行job 将超时收货、超时付款订单 做相应处理
    protected void execute()  {
        if(flag){
            flag = false;
            JsonObject jsonObject = new JsonObject();
            logger.info("============================"+ DateTime.getNowTime("yyyy-MM-dd HH:mm:ss")+" Job任务（超时收货、超时付款订单）开始执行....");
            jobsService.queryOrderCollect(jsonObject);
            logger.info("============================" + DateTime.getNowTime("yyyy-MM-dd HH:mm:ss") + " Job任务（超时收货、超时付款订单）执行结束....");
            flag = true;
        }
    }
}
