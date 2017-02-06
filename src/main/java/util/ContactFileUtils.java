package util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class ContactFileUtils {
    public final static String UPLOAD_PATH;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("fileUpload");
        UPLOAD_PATH = bundle.getString("uploadPath");
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix, new File(UPLOAD_PATH));
    }

    public static String getSystemFilePath(String fileName) {
        if(fileName != null) {
            return UPLOAD_PATH + fileName;
        }
        return null;
    }


    public static String getFileNameFromUrl(String url, String filePrefix) {
        if (StringUtils.contains(url, filePrefix)) {
            return filePrefix + StringUtils.substringAfter(url, filePrefix);
        } else {
            return null;
        }
    }

    public static synchronized boolean deleteFileByUrl(String url, String filePrefix) {
        String fileName = getFileNameFromUrl(url, filePrefix);
        boolean deleteResult = false;
        if (fileName != null) {
            deleteResult = new File(getSystemFilePath(fileName)).delete();
        }
        return deleteResult;
    }
}
