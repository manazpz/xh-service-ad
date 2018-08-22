package aq.common.serializer;

import aq.common.other.Rtn;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by ywb on 2016-11-21.
 */
public class SerializerRtn implements JsonSerializer<Rtn> {

    public JsonElement serialize(Rtn rtn, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("code",context.serialize(rtn.getCode()));
        object.add("service",context.serialize(rtn.getService()));
        object.add("message",context.serialize(rtn.getMessage()));
        object.add("logId",context.serialize(rtn.getLogId()));
        object.add("from",context.serialize(rtn.getFrom()));
        object.add("data",rtn.getData());
        return object;
    }
}
