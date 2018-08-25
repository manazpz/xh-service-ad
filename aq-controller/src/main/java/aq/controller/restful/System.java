package aq.controller.restful;


import aq.common.annotation.Permission;
import aq.common.other.Rtn;
import aq.common.util.FileUpload;
import aq.common.util.HttpUtil;
import aq.common.util.StringUtil;
import aq.service.system.Func;
import aq.service.system.SystemService;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.PrintWriter;


@Controller
@RequestMapping("/sys")
public class System extends Base {

    @Resource
    protected SystemService systemService;

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public void login(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
         writerJson(response,out,systemService.queryLogin(requestJson));
    }

    //登出
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST)
    @ResponseBody
    public void loginOut(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        Rtn rtn = new Rtn("System");
        rtn.setCode(200);
        rtn.setMessage("登出成功！");
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

    //刷新token
    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    @ResponseBody
    public void refreshToken(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.refreshToken(requestJson));
    }

    //region 用户
    @RequestMapping(value = "/user/info",method = RequestMethod.GET)
    public void userInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,systemService.queryUserInfo(jsonObject));
    }
    //endregion

    //修改密码
    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    @ResponseBody
    public void changePwd(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.updatePassword(requestJson));
    }

    //重置密码
    @RequestMapping(value = "/resetPwd",method = RequestMethod.POST)
    @Permission(RequireLogin=true, value = {"78496770D-6772-4CC2-9508-D08B8DD880DB"},name = {"管理员"})
    @ResponseBody
    public void resetPwd(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.resetPassword(requestJson));
    }

    //上传头像
    @RequestMapping(value = "/uploadHead",method = RequestMethod.POST)
    @ResponseBody
    public void uploadHead(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile haedImg = multipartRequest.getFile("avatar");
        FileUpload fileUpload = new FileUpload();
        String suffix = haedImg.getOriginalFilename().substring(haedImg.getOriginalFilename().lastIndexOf(".") + 1);
        String url = fileUpload.upload(haedImg, "","","/head", request);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("size",haedImg.getSize());
        jsonObject.addProperty("extend",suffix);
        jsonObject.addProperty("url",url);
        writerJson(response,out,systemService.uploadImg(jsonObject));
    }

    //上传Logo
    @RequestMapping(value = "/uploadLoge",method = RequestMethod.POST)
    @ResponseBody
    public void uploadLoge(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        Rtn rtn = new Rtn("System");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile haedImg = multipartRequest.getFile("avatar");
        FileUpload fileUpload = new FileUpload();
        String suffix = haedImg.getOriginalFilename().substring(haedImg.getOriginalFilename().lastIndexOf(".") + 1);
        String url = fileUpload.upload(haedImg, "favicon", "jpg","", request);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("logo", url);
        rtn.setCode(200);
        rtn.setMessage("success");
        rtn.setData(jsonObject);
        writerJson(response,out,Func.functionRtnToJsonObject.apply(rtn));
    }

    //读取资源文件
    @RequestMapping(value = "/getReasourse/{file}",method = RequestMethod.GET)
    public void getReasourse(@PathVariable(value = "file") String img, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-img");
        FileUpload fileUpload = new FileUpload();
        String suffix = request.getPathInfo().split(img)[1];
        if (StringUtil.isEmpty(suffix)) return;
        try {
            FileInputStream writeInput = fileUpload.write("/head/" + img + suffix);
            ServletOutputStream out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            while ((b = writeInput.read(buffer)) != -1) {
                // 4.写到输出流(out)中
                out.write(buffer, 0, b);
            }
            writeInput.close();
            out.flush();
            out.close();
        }catch (Exception ex) {
            return;
        }
    }

    //系统开关列表
    @RequestMapping(value = "/switchs",method = RequestMethod.GET)
    public void switchs(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        JsonObject jsonObject = HttpUtil.getParameterMap(request);
        writerJson(response,out,systemService.querySwitch(jsonObject));
    }

    //开关设置
    @RequestMapping(value = "/switch",method = RequestMethod.POST)
    @ResponseBody
    public void updateSwitch(@RequestBody JsonObject requestJson, HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        writerJson(response,out,systemService.uploadSwitch(requestJson));
    }

}
