package aq.controller.restful.management;

import aq.common.constants.APPConstants;
import aq.common.other.Rtn;
import aq.common.util.DbOperate;
import aq.common.util.GsonHelper;
import aq.service.system.Func;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/dataBase")
public class DataBaseController extends aq.controller.restful.System {
//    "F:/sql/"

    @Value("#{jdbcConfig['jdbc.master.username']}")
    private String SQL_ROOT;
    @Value("#{jdbcConfig['jdbc.master.password']}")
    private String SQL_PWD;
    @Value("#{jdbcConfig['jdbc.master.backname']}")
    private String SQL_BACK_NAME;


    //获取备份信息
    @RequestMapping(value = "/backups/list",method = RequestMethod.GET)
    public void backupsList(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws Exception {
        Rtn rtn = new Rtn("dataBase");
        JsonObject data = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        List fileName = DbOperate.getFileName(APPConstants.SQL_PATH);
        jsonArray =  GsonHelper.getInstanceJsonparser().parse(GsonHelper.getInstance().toJson(fileName)).getAsJsonArray();
        rtn.setCode(200);
        rtn.setMessage("success");
        data.add("items",jsonArray);
        rtn.setData(data);
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

    @RequestMapping(value = "/backups",method = RequestMethod.POST)
    @ResponseBody
    public void backups(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        Rtn rtn = new Rtn("dataBase");
        String backName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+".sql";
        try {
            DbOperate.dbBackUp(SQL_ROOT,SQL_PWD,SQL_BACK_NAME, APPConstants.SQL_PATH,backName);
            rtn.setCode(200);
            rtn.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            rtn.setCode(404);
            rtn.setMessage("备份失败");
        }
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

    @RequestMapping(value = "/recovery",method = RequestMethod.POST)
    @ResponseBody
    public void recovery(@RequestBody JsonObject requestJson,HttpServletRequest request, HttpServletResponse response, PrintWriter out){
        Rtn rtn = new Rtn("dataBase");
        try {
            DbOperate.dbRestore(SQL_ROOT,SQL_PWD,SQL_BACK_NAME,APPConstants.SQL_PATH+requestJson.get("fileName").getAsString());
            rtn.setCode(200);
            rtn.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            rtn.setCode(404);
            rtn.setMessage("备份失败");
        }
        writerJson(response,out, Func.functionRtnToJsonObject.apply(rtn));
    }

}
