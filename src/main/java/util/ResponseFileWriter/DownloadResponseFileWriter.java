package util.ResponseFileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DownloadResponseFileWriter extends ResponseFileWriterDecorator {
    private String dialogFileName;

    public DownloadResponseFileWriter(ResponseFileWriter instanse, String dialogFileName) {
        super(instanse);
        this.dialogFileName = dialogFileName;
    }

    @Override
    public void writeFileToResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", dialogFileName));
        instanse.writeFileToResponse(request, response);
    }
}
