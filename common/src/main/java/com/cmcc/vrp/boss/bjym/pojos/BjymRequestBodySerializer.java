package com.cmcc.vrp.boss.bjym.pojos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 北京云漫的JSON序列化规则, 顺序固定
 *
 * Created by sunyiwei on 2017/4/6.
 */
public class BjymRequestBodySerializer implements JsonSerializer<BjymRequestBody> {
    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     *
     * <p>In the implementation of this call-back method, you should consider invoking {@link
     * JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(BjymRequestBody src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("userdataList", context.serialize(src.getUserDataList()));
        jsonObject.addProperty("size", src.getSize());
        jsonObject.addProperty("type", src.getType());
        jsonObject.addProperty("requestid", src.getRequestId());

        return jsonObject;
    }
}
