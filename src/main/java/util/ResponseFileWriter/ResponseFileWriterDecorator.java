package util.ResponseFileWriter;

public abstract class ResponseFileWriterDecorator implements ResponseFileWriter {
    protected ResponseFileWriter instanse;

    public ResponseFileWriterDecorator(ResponseFileWriter instanse) {
        this.instanse = instanse;
    }
}
