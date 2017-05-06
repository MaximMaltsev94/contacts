package util.ResponseFileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ResponseFileWriter {
    void writeFileToResponse(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
