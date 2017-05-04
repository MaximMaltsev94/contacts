package util;

import exceptions.InvalidUrlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteImageDownloader {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteImageDownloader.class);
    private URL url;

    public RemoteImageDownloader(String url) throws InvalidUrlException {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            LOG.error("invalid url - {}", url, e);
            throw new InvalidUrlException("invalid url - " + url, e);
        }
    }

    public BufferedImage download() throws IOException {
        return ImageIO.read(url);
    }
}
