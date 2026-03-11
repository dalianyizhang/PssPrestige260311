package utils;

public class StringUtils {
    /**
     * 规范参数格式:将获取到的String串规范格式，空格' '换成下划线'_',然后转为全大写
     *
     * @param str 4项
     * @return
     */
    public static String formatStr4Enum(String str) {
        return str.replaceAll(" ", "_").toUpperCase();
    }

    /**
     * 规范参数格式:将获取到的String数字转换为Float
     *
     * @param str 8项
     * @return
     */
    public static float parseFloat(String str) {
        return Float.parseFloat(str.strip());
    }

    /**
     * 规范参数格式:将获取到的String数字转换为Int
     *
     * @param str 4项
     * @return
     */
    public static int parseInt(String str) {
        return Integer.parseInt(str.strip());
    }

}
