package util;

import java.io.IOException;
import java.util.Properties;

public class VkProperties {
    private int apllicationId;
    private String display;
    private String redirect_uri;
    private String scope;
    private String response_type;
    private String apiVersion;
    private String appSecret;
    private String serviceSecret;

    public VkProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("../importVK.properties"));
        apllicationId = Integer.parseInt(properties.getProperty("client_id"));
        display = properties.getProperty("display");
        redirect_uri = properties.getProperty("redirect_uri");
        scope = properties.getProperty("scope");
        response_type = properties.getProperty("response_type");
        apiVersion = properties.getProperty("v");
        appSecret = properties.getProperty("app_secret");
        serviceSecret = properties.getProperty("service_secret");
    }

    public int getApllicationId() {
        return apllicationId;
    }

    public String getDisplay() {
        return display;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getScope() {
        return scope;
    }

    public String getResponse_type() {
        return response_type;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getServiceSecret() {
        return serviceSecret;
    }
}
