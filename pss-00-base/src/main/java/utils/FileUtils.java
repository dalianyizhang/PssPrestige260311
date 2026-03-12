package utils;

import lombok.extern.log4j.Log4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Log4j
public class FileUtils {
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    public static boolean saveInputStreamToFile(InputStream inputStream, String filePath) {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getParentDirectory(String filePath) {
        return new File(filePath).getParent();
    }

    public static void deleteDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.error("删除文件失败: " + path, e);
                        }
                    });
        }
    }
}