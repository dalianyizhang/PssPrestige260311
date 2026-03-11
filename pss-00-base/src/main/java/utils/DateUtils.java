package utils;

import lombok.extern.log4j.Log4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Log4j
public class DateUtils {

    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final String DATE_FORMAT2 = "yyyy";

    private static final String DATE_FORMAT3 = "MMdd";

    public static String getLastUpdateDate() {
        // 0101 0401 0701 1001
        Date now = new Date();
        String year = new SimpleDateFormat(DATE_FORMAT2).format(now);
        int mdInt = Integer.parseInt(new SimpleDateFormat(DATE_FORMAT3).format(now));
        // log.info(mdInt);
        if (mdInt >= 1001) {
            return year + "1001";
        } else if (mdInt >= 701) {
            return year + "0701";
        } else if (mdInt >= 401) {
            return year + "0401";
        } else {
            return year + "0101";
        }
    }

    /**
     * 获取当前日期的时间戳
     *
     * @return
     */
    public static String getNowDate() {
        Date now = new Date();
        return new SimpleDateFormat(DATE_FORMAT).format(now);
    }

    /**
     * 获取指定日期到今天的天数
     *
     * @param dateStr
     * @return
     */
    public static Integer calcDays2Today(String dateStr) {
        return calcDays(dateStr, getNowDate());
    }

    /**
     * 获取两日期间隔的天数
     *
     * @param dateStr1
     * @param dateStr2
     * @return
     */
    public static Integer calcDays(String dateStr1, String dateStr2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate firstDate = LocalDate.parse(dateStr1, formatter);
        LocalDate secondDate = LocalDate.parse(dateStr2, formatter);

        // 3. 使用Java日期API计算两个日期之间的天数差
        return (int) ChronoUnit.DAYS.between(firstDate, secondDate);
    }

}
