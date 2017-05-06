package util.ResponseFileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseFileWriterImpl implements ResponseFileWriter {
    private File file;

    public ResponseFileWriterImpl(File file) {
        this.file = file;
    }

    @Override
    public void writeFileToResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mimeType = request.getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType + ";charset=UTF-8");
        response.setContentLength((int)file.length());

        try(FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}
