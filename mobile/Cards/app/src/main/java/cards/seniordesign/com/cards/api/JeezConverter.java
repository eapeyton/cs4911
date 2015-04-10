package cards.seniordesign.com.cards.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class JeezConverter implements retrofit.converter.Converter {
    private static final String SUCCESS_FIELD = "success";
    private static final String ERRORS_FIELD = "errors";
    private static Gson gson;
    private final JsonParser parser;
    private String charset;

    public JeezConverter() {
        this.parser = new JsonParser();
        this.charset = "UTF-8";
    }

    private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
        }
        return gson;
    }

    public static JSONObject toJson(Object obj) {
        try {
            return new JSONObject(getGson().toJson(obj));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(JSONObject obj, Class<T> clazz) {
        return getGson().fromJson(obj.toString(), clazz);
    }

    @Override public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String charset = this.charset;
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType(), charset);
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(body.in(), charset);
            return fromBody(isr, type);
        } catch (IOException e) {
            throw new ConversionException(e);
        } catch (JsonParseException e) {
            throw new ConversionException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private Object fromBody(InputStreamReader isr, Type type) {

        JsonObject obj = parser.parse(isr).getAsJsonObject();

        if (obj.has(SUCCESS_FIELD)) {
            checkForErrors(obj);
            if (obj.entrySet().size() == 1) {
                return obj.get(SUCCESS_FIELD).getAsBoolean();
            }
            obj.remove(SUCCESS_FIELD);
        }

        if (obj.entrySet().size() == 1) {
            JsonElement singleElement = obj.entrySet().iterator().next().getValue();
            return getGson().fromJson(singleElement, type);
        }

        return getGson().fromJson(obj, type);
    }

    private void checkForErrors(JsonObject obj) {
        boolean wasFailure = !obj.get(SUCCESS_FIELD).getAsBoolean();
        if (wasFailure) {
            if (obj.has(ERRORS_FIELD)) {
                String errors = obj.get(ERRORS_FIELD).toString();
                throw new RuntimeException("API Response failed and returned an error:" + errors);
            } else {
                throw new RuntimeException("API Response failed and did not return an error:" + obj.toString());
            }
        }
    }

    @Override public TypedOutput toBody(Object object) {
        try {
            JsonElement innerElement = getGson().toJsonTree(object);
            JsonObject outerObj = new JsonObject();
            outerObj.add(object.getClass().getSimpleName().toLowerCase(), innerElement);
            return new JsonTypedOutput(getGson().toJson(outerObj).getBytes(charset), charset);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }

        @Override public String fileName() {
            return null;
        }

        @Override public String mimeType() {
            return mimeType;
        }

        @Override public long length() {
            return jsonBytes.length;
        }

        @Override public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
}