package aq.controller.restful.api;

import aq.common.util.DateTime;
import aq.common.util.GsonHelper;
import aq.common.util.HttpUtil;
import aq.service.system.AddressService;
import aq.service.system.ConfigService;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
            String backUrl="http://m.xh.huanyibu.com/";
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
        JsonObject parameterMap = HttpUtil.getParameterMap(request);
        Map<String,Object> res = new HashMap<>();
        res.put("platform","wx");
        List<Map<String, Object>> maps = configService.selectTppConfig(res);
        //第一步：通过code换取网页授权access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+maps.get(0).get("accessKeyId")
                + "&secret="+maps.get(0).get("accessKeySecret")
                + "&code="+parameterMap.get("code")
                + "&grant_type=authorization_code";
        Gson gson = new Gson();
        Map<String, Object> mapInfo = gson.fromJson(new JsonParser().parse(HttpUtil.get(url)).getAsJsonObject(), new TypeToken<Map<String, Object>>() {
        }.getType());
        String openid = mapInfo.get("openid").toString();
        String access_token = mapInfo.get("access_token").toString();
        String refresh_token = mapInfo.get("refresh_token").toString();
        if("".equals(maps.get(0).get("accessToken")) || maps.get(0).get("accessToken") == null){
            mapInfo.clear();
            mapInfo.put("access_token",access_token);
            mapInfo.put("update_time", DateTime.dateFormat(new Date(), DateTime.DATE_FORMAT_YYYY_MM_DDHHMMSS));
            mapInfo.put("id",maps.get(0).get("ID"));
            configService.updateTppConfig(mapInfo);
        }
        // 第二步：拉取用户信息(需scope为 snsapi_userinfo)
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token
                + "&openid="+openid
                + "&lang=zh_CN";
        Map<String, Object> userInfo = gson.fromJson(new JsonParser().parse(HttpUtil.get(infoUrl)).getAsJsonObject(), new TypeToken<Map<String, Object>>() {
        }.getType());
        res.clear();
        res.put("openid",userInfo.get("openid"));
        res.put("nickname",userInfo.get("nickname"));
        res.put("head_portrait",userInfo.get("headimgurl"));
//        PageInfo pageInfo = userService.queryUser(res);
//        if(pageInfo.getList().size()<=0){
//            userService.insertUser(res);
//        }
        res.clear();
        res.put("code","0");
        return responseJson(response,out, res);
    }



}
