package util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static void setMessageText(HttpServletRequest request, String text, TooltipType type) {
        request.getSession().setAttribute("tooltip-type", type.toString());
        request.getSession().setAttribute("tooltip-text", text);
    }
}
