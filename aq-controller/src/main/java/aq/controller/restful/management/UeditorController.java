package aq.controller.restful.management;


import com.baidu.ueditor.ActionEnter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/ueditor")
public class UeditorController extends aq.controller.restful.System {


    @ResponseBody
    @RequestMapping(value = "uploadImage", method={RequestMethod.GET, RequestMethod.POST})
    public void  uploadImage(HttpServletRequest request, HttpServletResponse response, String action) {

        String rootPath = request.getSession().getServletContext().getRealPath("/");

        try {
            if("config".equals(action)){    //如果是初始化
                String exec = new ActionEnter(request, rootPath).exec();
                PrintWriter writer = response.getWriter();
                writer.write(exec);
                writer.flush();
                writer.close();
            }else if("uploadimage".equals(action) || "uploadvideo".equals(action) || "uploadfile".equals(action)){    //如果是上传图片、视频、和其他文件
                try {
                    MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
                    MultipartHttpServletRequest Murequest = resolver.resolveMultipart(request);
                    Map<String, MultipartFile> files = Murequest.getFileMap();//得到文件map对象
                    // 实例化一个jersey
                    Client client = new Client();

                    for(MultipartFile pic: files.values()){
                        JSONObject jo = new JSONObject();
                        long size = pic.getSize();    //文件大小
                        String originalFilename = pic.getOriginalFilename();  //原来的文件名
                        String uploadInfo = upload(client, pic, request, response, "http://localhost:8083/", "images");
                        if(!"".equals(uploadInfo)){    //如果上传成功
                            String[] infoList = uploadInfo.split(";");
                            jo.put("state", "SUCCESS");
                            jo.put("original", originalFilename);//原来的文件名
                            jo.put("size", size);//文件大小
                            jo.put("title", infoList[1]);//随意，代表的是鼠标经过图片时显示的文字
                            jo.put("type", FilenameUtils.getExtension(pic.getOriginalFilename()));//文件后缀名
                            jo.put("url", infoList[2]);//这里的url字段表示的是上传后的图片在图片服务器的完整地址（http://ip:端口/***/***/***.jpg）
                        }else{    //如果上传失败
                        }
                        renderJson(response, jo.toString());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }

    }

    public static void renderJson(HttpServletResponse response, String text){
        render(response, "application/json;charset=utf-8", text);
    }

    //发送内容
    public static void render(HttpServletResponse response, String contentType, String text){
        response.setContentType(contentType);
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String upload(Client client, MultipartFile file, HttpServletRequest request,HttpServletResponse response, String serverPath, String path){
        // 文件名称生成策略（日期时间+uuid ）
        UUID uuid = UUID.randomUUID();
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate = format.format(d);
        // 获取文件的扩展名
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // 文件名
        String fileName = formatDate + "-" + uuid + "." + extension;
        //相对路径
        String relaPath = path + fileName;

        String a = serverPath + path.substring(0, path.lastIndexOf("/"));
        File file2 = new File(a);
        if(!file2.exists()){
            boolean mkdirs = file2.mkdirs();
            System.out.println(mkdirs);
        }

        // 另一台tomcat的URL（真实路径）
        String realPath = serverPath + relaPath;
        // 设置请求路径
        WebResource resource = client.resource(realPath);

        // 发送开始post get put（基于put提交）
        try {
            resource.put(String.class, file.getBytes());
            return fileName+";"+relaPath+";"+realPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
