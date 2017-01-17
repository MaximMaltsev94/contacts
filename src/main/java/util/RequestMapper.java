package util;

import exceptions.RequestMapperException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class RequestMapper {
    private final static Logger LOG = LoggerFactory.getLogger(RequestMapper.class);

    private final int sizeThreshold;
    private final long maxFileSize;
    private final long maxRequestSize;
    public RequestMapper() {
        ResourceBundle bundle = ResourceBundle.getBundle("fileUpload");
        sizeThreshold = Integer.parseInt(bundle.getString("sizeThreshold"));
        maxFileSize = Long.parseLong(bundle.getString("maxFileSize"));
        maxRequestSize = Long.parseLong(bundle.getString("maxRequestSize"));
    }

    public void mapRequestParamsToAttributes(HttpServletRequest request) throws RequestMapperException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(isMultipart) {
            mapMultipartParamsToAttributes(request);
        } else {
            mapRegularParamsToAttributes(request);
        }
    }

    private void mapMultipartParamsToAttributes(HttpServletRequest request) throws RequestMapperException {
        File repository = (File) request.getServletContext().getAttribute("javax.servlet.context.tempdir");

        DiskFileItemFactory factory = new DiskFileItemFactory(sizeThreshold, repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxRequestSize);
        upload.setFileSizeMax(maxFileSize);

        try {
            List<FileItem> itemList = upload.parseRequest(request);
            for (FileItem fileItem : itemList) {
                if (fileItem.isFormField()) {
                    request.setAttribute(fileItem.getFieldName(), fileItem.getString());
                } else {
                    request.setAttribute(fileItem.getFieldName(), fileItem.getInputStream());
                }
            }
        } catch (FileUploadException e) {
            LOG.error("can't parse multipart request", e);
            throw new RequestMapperException("error while parsing multipart parameters", e);
        } catch (IOException e) {
            LOG.error("can't get input stream for multipart file item", e);
            throw new RequestMapperException("error while getting input stream for file item", e);
        }
    }

    private void mapRegularParamsToAttributes(HttpServletRequest request) {
        request.getParameterMap().forEach((key, values) -> request.setAttribute(key, request.getParameter(key)));
    }


}
