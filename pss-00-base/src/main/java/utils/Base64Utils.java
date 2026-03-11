package utils;

import cn.hutool.core.codec.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base64Utils {

    /**
     * 文件转base64
     * @param absPath: 文件的绝对路径
     * @return base64编码 异常时返回null
     */
    public static String getBase64ByAbsPath(String absPath){
        File file = new File(absPath);
        if(!file.exists()){
            return null;
        }
        return Base64.encode(file);
    }

    /**
     * base64转文件 并存在本地
     * @param absPath base64转换文件后存储的绝对路径
     * @param base64 要转换文件的base64编码
     * @throws IOException
     */
    public static void base64ToFile(String absPath, String base64)throws IOException {
        byte[] decodedBytes = Base64.decode(base64);
        Files.write(Paths.get(absPath), decodedBytes);
    }
}
