package util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ContactUtils {
    public static synchronized String getUTF8String(String target) throws UnsupportedEncodingException {
        String result = null;
        if (target != null)
            result = new String(target.getBytes("iso-8859-1"), "UTF-8");
        return result;
    }

    public static synchronized List<FileItem> getMultipartItems(HttpServletRequest request, int maxFileSize) throws FileUploadException {
        DiskFileItemFactory factory = new DiskFileItemFactory();

        ServletContext servletContext = request.getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        factory.setSizeThreshold(maxFileSize);

        ServletFileUpload upload = new ServletFileUpload(factory);
        return upload.parseRequest(request);
    }


    public static String getFileNameFromUrl(String url, String filePrefix) {
        if (StringUtils.contains(url, filePrefix)) {
            return filePrefix + StringUtils.substringAfter(url, filePrefix);
        } else {
            return null;
        }
    }

    public static synchronized boolean deleteFileByUrl(String url, String uploadPath, String filePrefix) {
        String fileName = getFileNameFromUrl(url, filePrefix);
        boolean deleteResult = false;
        if (fileName != null) {
            deleteResult = new File(uploadPath + fileName).delete();
        }
        return deleteResult;
    }
}
