/**
 * Created by 杨大山 on 2017-03-25.
 */
package aq.common.util;

import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public final class HttpUtil {
    private HttpUtil() {}
    public static String get(String urlPath) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Charset","utf-8");
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {

            }

            InputStream is = conn.getInputStream();

            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) != -1) {
                sb.append(new String(buff, 0, len));
            }
            is.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * post方式请求，参数默认不转码
     * @param urlStr
     * @param param  注意顺序 [参数,格式，timeout,readtime]
     * @return
     */
    public static String post(String urlStr, String... param) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type",(param.length>1)?param[1]:"application/x-www-form-urlencoded");
            con.setConnectTimeout((param.length > 2) ? Integer.parseInt(param[2]) : 60 * 1000);
            con.setReadTimeout((param.length>3)?Integer.parseInt(param[3]):60*1000);
            con.connect();

            DataOutputStream dos=new DataOutputStream(con.getOutputStream());
            dos.writeBytes(param[0]);
            dos.flush();
            dos.close();
            int responseCode = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * delete方式请求，参数默认不转码
     * @param urlStr
     * @param param  注意顺序 [参数,格式，timeout,readtime]
     * @return
     */
    public static String delete(String urlStr, String... param) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("DELETE");

            con.setRequestProperty("Content-Type",(param.length>1)?param[1]:"application/x-www-form-urlencoded");
            con.setConnectTimeout((param.length > 2) ? Integer.parseInt(param[2]) : 60 * 1000);
            con.setReadTimeout((param.length>3)?Integer.parseInt(param[3]):60*1000);
            con.connect();

            DataOutputStream dos=new DataOutputStream(con.getOutputStream());
            dos.writeBytes(param[0]);
            dos.flush();
            dos.close();
            int responseCode = con.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonObject getParameterMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        JsonObject returnObject = new JsonObject();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnObject.addProperty(name, value);
        }

        return returnObject;
    }
}

