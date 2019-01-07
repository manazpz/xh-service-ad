package aq.controller.restful.api;

import aq.common.util.*;
import aq.dao.news.NewsDao;
import aq.dao.order.OrderDao;
import aq.service.news.NewsApiService;
import aq.service.order.OrderApiService;
import aq.service.order.OrderService;
import aq.service.system.ConfigService;
import aq.service.system.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/api/wx")
public class WxController extends aq.controller.restful.System {

    @Resource
    protected ConfigService configService;

    @Resource
    protected UserService userService;

    @Resource
    protected OrderApiService orderApiService;

    @Resource
    protected OrderDao orderDao;

    @Resource
    protected NewsDao newsDao;

    /**
     * 获取微信公众号配置进行前端授权
     * @param request
     * @param response
     * @param out
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/config",method =RequestMethod.GET)
    public String queryCode(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        Map<String,Object> res = new HashMap<>();
        String url = "";
        res.put("platform","wx");
        List<Map<String, Object>> map = configService.selectTppConfig(res);
        if(map.size()>0){
            //这个url的域名必须要进行再公众号中进行注册验证，这个地址是成功后的回调地址
            String backUrl=map.get(0).get("returnUrl")+"/index.html";
            // 第一步：用户同意授权，获取code
            url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+map.get(0).get("accessKeyId")
                    + "&redirect_uri="+ URLEncoder.encode(backUrl)
                    + "&response_type=code"
                    + "&scope=snsapi_userinfo"
                    + "&state=STATE#wechat_redirect";
        }
        res.clear();
        res.put("url",url);
        return responseJson(response,out, res);//必须重定向，否则不能成功
    }

    /**
     * 获取微信用户基本信息
     * @param request
     * @param response
     * @param out
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userinfo",method =RequestMethod.GET)
    public String queryUserMsg(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Gson gson = new Gson();
        Map parameterMap = HttpUtil.getParameterMaps(request);
        Map<String,Object> res = new HashMap<>();
        res.put("platform","wx");
        List<Map<String, Object>> maps = configService.selectTppConfig(res);
        //第一步：通过code换取网页授权access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+maps.get(0).get("accessKeyId")
                + "&secret="+maps.get(0).get("accessKeySecret")
                + "&code="+parameterMap.get("code")
                + "&grant_type=authorization_code";
        Map<String, Object> mapInfo = gson.fromJson(new JsonParser().parse(HttpUtil.get(url)).getAsJsonObject(), new TypeToken<Map<String, Object>>() {
        }.getType());
        String openid = mapInfo.get("openid").toString();
        String access_token = mapInfo.get("access_token").toString();
        String refresh_token = mapInfo.get("refresh_token").toString();
        // 第二步：拉取用户信息(需scope为 snsapi_userinfo)
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token
                + "&openid="+openid
                + "&lang=zh_CN";
        Map<String, Object> userInfo = gson.fromJson(new JsonParser().parse(HttpUtil.get(infoUrl)).getAsJsonObject(), new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.print("userInfo=========="+userInfo);
        res.clear();
        res.put("openid",userInfo.get("openid"));
        res.put("nickname",userInfo.get("nickname"));
        res.put("head_portrait",userInfo.get("headimgurl"));
        double sex = Double.parseDouble(userInfo.get("sex").toString());
        res.put("sex",(int)Math.ceil(sex));
        res.put("country",userInfo.get("country"));
        res.put("province",userInfo.get("province"));
        res.put("city",userInfo.get("city"));
        List<Map<String, Object>> mapsuser = userService.queryUserInfos(res);
        if(mapsuser.size()<=0){
            userService.insertUserInfos(res);
        }else{
            userService.updateUserInfos(res);
        }
        res.clear();
        res.put("code",200);
        res.put("openid",userInfo.get("openid"));
        return responseJson(response,out, res);
    }

    /**
     * 微信统一下单
     * @param request
     * @param response
     * @param out
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay",method = RequestMethod.POST)
    @ResponseBody
    public void Pay(@RequestBody JsonObject requestJson ,HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        requestJson.addProperty("remortIP",WxUtil.getRemortIP(request));
        writerJson(response,out,configService.pay(requestJson));
    }

    /**
     * 微信支付回调
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, value = "/notify")
    public String wxCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = "";
        Map<String, String> backxml = new HashMap<String, String>();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> rest = new HashMap<>();
        JsonObject jsonObject = new JsonObject();
        InputStream inStream;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
            Map<String, String> map = WxUtil.xmlToMap(result);
            if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
                //业务处理开始
                res.put("number",map.get("out_trade_no"));
                System.out.print("=============支付回调=================："+map);
                List<Map<String, Object>> maps = orderApiService.selectOrder(res);
                if(maps.size()>0){
                    System.out.print("=============执行更新订单=================");
                    res.clear();
                    res.put("paystatus","02");
                    res.put("id",maps.get(0).get("id"));
                    res.put("lastCreateTime",new Date());
                    orderDao.updateOrder(res);
                    rest.put("id",UUIDUtil.getUUID());
                    rest.put("orderId",res.get("id"));
                    rest.put("buyer",maps.get(0).get("buyerId"));
                    rest.put("createTime",new Date());
                    rest.put("status","02");
                    rest.put("type","01");
                    newsDao.insertOrderLog(rest);
                    rest.clear();
                    rest.put("id",UUIDUtil.getUUID());
                    rest.put("orderId",map.get("out_trade_no"));
                    rest.put("types",maps.get(0).get("type"));
                    rest.put("total_fee",map.get("total_fee"));
                    rest.put("payWay","01");
                    rest.put("createUserId",maps.get(0).get("buyerId"));
                    rest.put("createTime",new Date());
                    orderDao.insertBlance(rest);
                }
                //业务处理结束
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resXml;


    }


}
