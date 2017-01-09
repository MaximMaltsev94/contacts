package servlet.filters;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("text/html; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        servletRequest.getParameterMap().forEach((key, values) -> {

        });
        chain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
