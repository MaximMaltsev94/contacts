package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(RequestUtils.class);

    public static void setMessageText(HttpServletRequest request, String text, TooltipType type) {
        request.getSession().setAttribute("tooltip-type", type.toString());
        request.getSession().setAttribute("tooltip-text", text);
    }

    public static String getParametersString(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getAttributeNames();
        Map<String, Object> parameterMap = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, request.getAttribute(name));
        }

        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            result = objectMapper.writeValueAsString(parameterMap);
        } catch (IOException e) {
            LOG.error("error while converting request parameters to json", e);
        }
        return result;
    }
}
