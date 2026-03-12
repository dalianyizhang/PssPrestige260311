package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    public static BufferedImage cropImage(String imagePath, int x, int y, int width, int height) {
        try {
            BufferedImage original = ImageIO.read(new File(imagePath));
            if (original != null) {
                // 防止裁剪区域越界
                int realWidth = Math.min(width, original.getWidth() - x);
                int realHeight = Math.min(height, original.getHeight() - y);
                return original.getSubimage(x, y, realWidth, realHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveImage(BufferedImage image, String filePath) {
        try {
            File output = new File(filePath);
            File parent = output.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            return ImageIO.write(image, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
