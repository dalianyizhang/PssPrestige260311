package utils;

import lombok.extern.log4j.Log4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 目标：将数据保存为csv
 * crew - 按照星级分表 例：crew_1.csv
 * group - 按照材料星级分表 例如group_1.csv
 */
@Log4j
public class CSVUtils {

    public static void writeCsv(String data, String outputPath, String fileName) {
        try {
            File saveDir = new File(outputPath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            String fullFilePath = outputPath + File.separator + fileName.replace(".csv","") + ".csv";
            FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            if (null != data) {
                // 写入UTF-8-bom头，解决excel打开UTF-8格式的中文乱码的问题
                outputStreamWriter.write("\uFEFF");
                //写入数据
                outputStreamWriter.write(data);
            }

            outputStreamWriter.close();
            fileOutputStream.close();

        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * FileOutputStream 默认是覆写指定文件的，除非设置append参数为true
     * @param data
     * @param outputPath
     * @param fileName
     */
    public static void writeCsv(List<String[]> data, String outputPath, String fileName) {
        try {
            File saveDir = new File(outputPath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            String fullFilePath = outputPath + File.separator + fileName + ".csv";
            FileOutputStream fileOutputStream = new FileOutputStream(fullFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            CSVFormat format = CSVFormat.DEFAULT;
            CSVPrinter printer = new CSVPrinter(outputStreamWriter, format);

            if (null != data) {
                // log.info(">>>>>>>>" + fileName + " dataList.length=" + data.size());
                // 写入UTF-8-bom头，解决excel打开UTF-8格式的中文乱码的问题
                outputStreamWriter.write("\uFEFF");
                //循环写入数据
                for (String[] lineData : data) {
                    printer.printRecord(lineData);
                }
            }
            printer.flush(); //流式输出最后记得刷新
            printer.close();
            outputStreamWriter.close();
            fileOutputStream.close();

        } catch (IOException e) {
            log.error(e);
        }
    }

    /**
     * 读取CSV文件为数据流
     * @param filePath
     * @return
     */
    public static List<CSVRecord> readCSV(String filePath) {
        List<CSVRecord> records = new ArrayList<>();
        try {
            CSVFormat format = CSVFormat.Builder.create()
                    .setHeader() // 读取header作为csv的key，否则CSVRecord.get(headerName)会报错
                    .setSkipHeaderRecord(true) // 跳过第一行的列名，列名单独是文件的自行搜索CSVFormat构造
                    .build();
            FileReader fileReader = new FileReader(filePath, StandardCharsets.UTF_8);
            CSVParser csvParser = new CSVParser(fileReader, format);
            records = csvParser.getRecords();

            csvParser.close();
            fileReader.close();

        } catch (IOException e) {
            // log.error(e);
        }
        return records;
    }

}
