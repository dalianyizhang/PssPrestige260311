package crawl;

import crawl.PSSCrawlFileAPI;
import utils.DateUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        String date = LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.BASIC_ISO_DATE);
        String downloadPath = "downloads";

        // 下载并解析“船员信息+合成信息”文件到本地
        PSSCrawlFileAPI.crawlCrewAndPrestige(downloadPath);
    }
}
