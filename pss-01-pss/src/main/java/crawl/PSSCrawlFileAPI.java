package crawl;

import Do.CrewDo;
import Do.PrestigeDo;
import lombok.extern.log4j.Log4j;
import pojo.Enums.Rarity;
import utils.CSVUtils;
import utils.CrawlHtml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Log4j
public class PSSCrawlFileAPI {

    public static Boolean connectTry() {
        String settingUrl = String.format(PssUrl.SETTING, PssUrl.BASE);
        String html = CrawlHtml.getHtmlBySelenium(settingUrl);
        boolean isInValidId = html.matches("[\\s\\S]*Invalid[\\s\\S]*");
        boolean isUnAccessed = html.matches("[\\s\\S]*访问错误[\\s\\S]*");
        // log.info(isInValidId);
        // log.info(isUnAccessed);
        // log.info(html);
        log.info("setting页面长度：" + html.length());
        return !(isInValidId || isUnAccessed);
    }

    /**
     * 进行全部的爬取工作
     */
    public static void crawlCrewAndPrestige(String outputPath) {
        String baseUrl = new PssUrl().getBaseUrl();
        String crewUrl = String.format(PssUrl.CREW_ALL, baseUrl);
        CrawlCrewsInfo crawlCrews = new CrawlCrewsInfo();
        CrawlPrestigeInfo crawlPrestige = new CrawlPrestigeInfo();

        // 留存最后结果=0的id，便于稍后尝试重新爬取
        List<Integer> preTo = new Vector<>();
        List<Integer> preFrom = new Vector<>();


        // 1.爬取船员基本信息
        // 1.1 原始数据
        List<CrewDo> crewDoList = crawlCrews.parseCrewList(crewUrl);
        // 1.2 数据清洗 去除机器人
        crewDoList = crewDoList.parallelStream()
                .filter(crewDo -> !CrawlPrestigeInfo.filter.contains(crewDo.getId()))
                .collect(Collectors.toCollection(Vector::new));
        // 1.3 统一存储
        List<String[]> crews = crewDoList.parallelStream()
                .map(CrewDo::getData4Str)
                .collect(Collectors.toCollection(Vector::new));
        crews.add(0, CrewDo.getHeader4Str());
        CSVUtils.writeCsv(crews, outputPath, "crew_all");
        // 1.4 按照稀有度 分类存储
        for (Rarity rarity : Rarity.values()) {
            List<String[]> records = crewDoList.parallelStream()
                    .filter(crewDo -> Objects.equals(crewDo.getRarity(), rarity.name_zh))
                    .collect(Collectors.toList())
                    .parallelStream().map(CrewDo::getData4Str).collect(Collectors.toCollection(Vector::new));
            records.add(0, CrewDo.getHeader4Str());
            CSVUtils.writeCsv(records, outputPath, "crew_" + rarity.index);
        }



        // 2.爬取合成信息(这里是prestigeTo)
        // 2.1 筛选掉1/6星的船员
        List<CrewDo> collect = crewDoList.parallelStream()
                .filter(crewDo -> Objects.equals(crewDo.getRarity(), Rarity.ELITE.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.UNIQUE.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.EPIC.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.HERO.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.LEGENDARY.name_zh))
                .collect(Collectors.toList());
        // 2.2 逐个船员爬取解析
        collect
                // .parallelStream()  // 不要启用！并行执行的大量查询会导致相当部分请求被拒绝，导致查询结果为空。
                .forEach(crewDo -> {
            String prestigeToUrl = String.format(PssUrl.PRESTIGE_TO, baseUrl, crewDo.getId());
            List<PrestigeDo> prestigeDoList = crawlPrestige.parsePrestigeList(prestigeToUrl);
            Vector<String[]> records = new Vector<>();
            if (prestigeDoList.size() > 0) {
                // 本地数据处理可以并行执行
                prestigeDoList.parallelStream().forEach(prestigeDo -> records.add(prestigeDo.getData4Str()));
            } else {
                preTo.add(crewDo.getId());
            }
            records.add(0, PrestigeDo.getHeader4Str());
            CSVUtils.writeCsv(records, outputPath, "prestige_to_" + crewDo.getId());
        });

        // 3.爬取 prestigeFrom的合成信息
        List<CrewDo> collect2 = crewDoList.parallelStream()
                .filter(crewDo -> Objects.equals(crewDo.getRarity(), Rarity.COMMON.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.ELITE.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.UNIQUE.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.EPIC.name_zh)
                        || Objects.equals(crewDo.getRarity(), Rarity.HERO.name_zh))
                .collect(Collectors.toList());
        collect2
                // .parallelStream() // 同上，尽量不要启用此处的并行处理！
                .forEach(crewDo -> {
            String prestigeFromUrl = String.format(PssUrl.PRESTIGE_FROM, baseUrl, crewDo.getId());
            List<PrestigeDo> prestigeDoList = crawlPrestige.parsePrestigeList(prestigeFromUrl);
            Vector<String[]> records = new Vector<>();
            if (prestigeDoList.size() > 0) {
                prestigeDoList.parallelStream()
                        // 最终数据查询时的去重处理
                        .filter(prestigeDo -> prestigeDo.getFirstSonId() <= prestigeDo.getSecondSonId())
                        .forEach(prestigeDo -> records.add(prestigeDo.getData4Str()));
            } else {
                preFrom.add(crewDo.getId());
            }
            records.add(0, PrestigeDo.getHeader4Str());
            CSVUtils.writeCsv(records, outputPath, "prestige_from_" + crewDo.getId());
        });

        // 4.写入valid_util.txt文件
        try {
            // 获取当天 UTC 日期，格式化为 yyyy-MM-dd
            String today = LocalDate.now(ZoneOffset.UTC).toString();  // 默认 ISO 格式
            // 构建文件路径
            Path filePath = Paths.get(outputPath, "valid_until.txt");
            // 确保目录存在（如果前面的操作没有创建目录，这里会自动创建）
            Files.createDirectories(filePath.getParent());
            // 写入内容
            Files.writeString(filePath, today);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 5.输出size=0 的id
        log.info("preTo(可能缺失):" + Arrays.toString(preTo.toArray()));
        log.info("preFrom(可能缺失):" + Arrays.toString(preFrom.toArray()));
    }
}
