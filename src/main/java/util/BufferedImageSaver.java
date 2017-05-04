package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BufferedImageSaver {
    private BufferedImage bufferedImage;

    public BufferedImageSaver(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public String saveToFileSystem(String prefix) throws IOException {
        File fileToSave = ContactFileUtils.createTempFile(prefix, ".png");
        ImageIO.write(bufferedImage, "png", fileToSave);
        return fileToSave.getName();
    }
}
