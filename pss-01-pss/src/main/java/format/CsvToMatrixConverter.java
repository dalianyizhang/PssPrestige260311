package format;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.csv.CSVRecord;
import utils.CSVUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static utils.StringUtils.parseInt;

/**
 * 用于将指定目录下的多个合成文件 按照星级依次从以为表格合并为二维表的格式 便于对比合成表是否发生变化
 * ps：仅使用prestige_from的数据
 */

@Log4j
public class CsvToMatrixConverter {

    public static final String FILE_PATH_CREW = "%s\\crew_%s.csv";
    public static final String FILE_PATH_PRESTIGE_FROM = "%s\\prestige_from_%s.csv";


    public static void main(String[] args) {
        // String csvPath = "F:\\1\\pss\\csv\\20241222";
        // String csvPath = "F:\\1\\pss\\csv\\20250102";
        String csvPath = "F:\\1\\pss\\csv\\20251001";
        CsvToMatrixConverter csvUtil = new CsvToMatrixConverter();

        List<Integer> rarityList = List.of(1, 2, 3, 4, 5);
        rarityList.forEach(rarity -> {
            // 1. 获取CrewDo列表
            List<SimpleCrewDo> crewList = csvUtil.queryCrewDoListByRarity(csvPath, String.valueOf(rarity));

            // 2. 生成文件列表
            List<File> csvFiles = crewList.stream()
                    .map(crew -> {
                        String fileName = String.format(FILE_PATH_PRESTIGE_FROM, csvPath, crew.getId());
                        return new File(fileName);
                    })
                    .collect(Collectors.toList());

            // 3. 处理文件并生成矩阵
            MatrixData result = csvUtil.convertToMatrix(csvFiles);

            // 4. 输出二维表CSV
            String outputFilePath = String.format("%s/prestige_%s_251001.csv", "F:\\1\\pss\\csv\\对比数据", rarity);
            csvUtil.saveAsCsv(result, outputFilePath);
        });
    }

    /**
     * 本地文件查询
     *
     * @param filePath
     * @param rarityMode
     * @return
     */
    public List<SimpleCrewDo> queryCrewDoListByRarity(String filePath, String rarityMode) {
        List<SimpleCrewDo> res = new ArrayList<>();
        List<CSVRecord> csvRecords = CSVUtils.readCSV(String.format(FILE_PATH_CREW, filePath, rarityMode));
        if (csvRecords.size() != 0) {
            csvRecords.forEach(record -> {
                res.add(new SimpleCrewDo(parseInt(record.get(0)), record.get(2), record.get(1)));
            });
        }
        return res;
    }

    public MatrixData convertToMatrix(List<File> csvFiles) {
        // 使用LinkedHashMap保持插入顺序
        Map<String, Map<String, String>> matrix = new HashMap<>();
        Set<Integer> allSonInts = new HashSet<>(); // 使用整数集合

        for (File file : csvFiles) {
            if (!file.exists()) {
                System.err.println("文件不存在: " + file.getPath());
                continue;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                br.readLine(); // 跳过表头

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;

                    String son1 = parts[0].trim();
                    String son2 = parts[1].trim();
                    String father = parts[2].trim();

                    // 添加整数ID到集合
                    allSonInts.add(Integer.parseInt(son1));
                    allSonInts.add(Integer.parseInt(son2));

                    // 构建二维映射
                    matrix.computeIfAbsent(son1, k -> new HashMap<>())
                            .put(son2, father);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Integer> sortedInts = new ArrayList<>(allSonInts);
        Collections.sort(sortedInts);

        // 转换为字符串列表
        List<String> sortedSons = sortedInts.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        return new MatrixData(matrix, sortedSons);
    }

    public void saveAsCsv(MatrixData data, String outputPath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            log.info(">>>>" + outputPath);
            List<String> sons = data.sonList;

            // 写入表头
            pw.print(",");
            pw.println(String.join(",", sons));

            // 写入数据行
            for (String rowSon : sons) {
                pw.print(rowSon);
                for (String colSon : sons) {
                    pw.print(",");
                    Map<String, String> row = data.matrix.get(rowSon);
                    // 获取值（不存在时填充空字符串）
                    String value = (row != null && row.containsKey(colSon))
                            ? row.get(colSon) : "";
                    pw.print(value);
                }
                pw.println();
            }
            // log.info("写入完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    @AllArgsConstructor
    public class SimpleCrewDo {
        private int id;
        private String rarity;
        private String name;
    }

    /**
     * 匿名内部类
     */
    @Data
    public class MatrixData {
        private Map<String, Map<String, String>> matrix;
        private List<String> sonList;

        MatrixData(Map<String, Map<String, String>> matrix, List<String> sonList) {
            this.matrix = matrix;
            this.sonList = sonList;
        }
    }
}
