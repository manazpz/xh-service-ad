package aq.common.interceptor;


import aq.common.util.GsonHelper;
import com.google.gson.*;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.lang.reflect.Type;

/**
 *    Message Converter Http请求中的JSON数据转换器
 *  * Spring MVC Json converter tools
 *    Created by yangwanbin on 2016-12-03.
 */
public class HttpMessageConverter  extends GsonHttpMessageConverter {

    public HttpMessageConverter(){
        //change json converter
        super.setGson(
                GsonHelper.getInstanceVip(Double.class,new JsonSerializer<Double>() {
                    @Override
                    public JsonElement serialize(Double aDouble, Type type, JsonSerializationContext context) {
                        if (aDouble == aDouble.longValue())
                            return new JsonPrimitive(aDouble.longValue());
                        return new JsonPrimitive(aDouble);
                    }
                })
        );
    }
}
