package util.request;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.RequestParamHandlerException;
import util.ContactUtils;

public class MultipartRequestParamHandler implements RequestParamHandler {
    private final static Logger LOG = LoggerFactory.getLogger(MultipartRequestParamHandler.class);
    
    private final int sizeThreshold;
    private final long maxFileSize;
    private final long maxRequestSize;

    public MultipartRequestParamHandler() throws IOException {
        Properties fileUploadProperties = new Properties();
        fileUploadProperties.load(getClass().getResourceAsStream("../../fileUpload.properties"));
        sizeThreshold = Integer.parseInt(fileUploadProperties.getProperty("sizeThreshold"));
        maxFileSize = Long.parseLong(fileUploadProperties.getProperty("maxFileSize"));
        maxRequestSize = Long.parseLong(fileUploadProperties.getProperty("maxRequestSize"));
    }
    
    public void handleRequestParams(HttpServletRequest request) throws RequestParamHandlerException {
        if(!ServletFileUpload.isMultipartContent(request)) {
            return;
        }
        File repository = (File) request.getServletContext().getAttribute("javax.servlet.context.tempdir");

        DiskFileItemFactory factory = new DiskFileItemFactory(sizeThreshold, repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxRequestSize);
        upload.setFileSizeMax(maxFileSize);

        try {
            List<FileItem> itemList = upload.parseRequest(request);
            for (FileItem fileItem : itemList) {
                if (fileItem.isFormField()) {
                    String elem = null;
                    if(StringUtils.isNotBlank(fileItem.getString())) {
                        elem = ContactUtils.getUTF8String(fileItem.getString());
                    }
                    request.setAttribute(fileItem.getFieldName(), elem);
                } else {
                    request.setAttribute(fileItem.getFieldName(), fileItem.getInputStream());
                    String fileExtension = "." + StringUtils.substringAfter(fileItem.getName(), ".");
                    request.setAttribute(fileItem.getFieldName() + ":fileExtension", fileExtension);
                }
            }
        } catch (FileUploadException e) {
            LOG.error("can't parse multipart request", e);
            throw new RequestParamHandlerException("error while parsing multipart parameters", e);
        } catch (IOException e) {
            LOG.error("can't get input stream for multipart file item", e);
            throw new RequestParamHandlerException("error while getting input stream for file item", e);
        }   
    }
}
