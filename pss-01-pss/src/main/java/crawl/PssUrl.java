package crawl;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.CrawlHtml;

@Log4j
@Data
public class PssUrl {
    public String baseUrl;
    public static final String BASE = "https://api.pixelstarships.com";
    public static final String BASE2 = "https://api2.pixelstarships.com";
    public static final String SETTING = "%s/SettingService/getLatestVersion3?languageKey=zh-CN&deviceType=DeviceTypeAndroid";
    public static final String CREW_ALL = "%s/CharacterService/ListAllCharacterDesigns2?languageKey=zh-CN";
    public static final String PRESTIGE_TO = "%s/CharacterService/PrestigeCharacterTo?characterDesignId=%s";
    public static final String PRESTIGE_FROM = "%s/CharacterService/PrestigeCharacterFrom?characterDesignId=%s";

    /**
     * 初始化pss官网地址 - baseUrl
     * pss正式服的数据会不定期在两个服务器之间切换，因此需要先确定当前提供服务的是哪一台服务器
     */
    private void init() {
        String html = CrawlHtml.getHtmlBySelenium(BASE + SETTING);
        boolean isInValidId = html.matches("[\\s\\S]*Invalid[\\s\\S]*");
        boolean isUnAccessed = html.matches("[\\s\\S]*访问错误[\\s\\S]*");
        if (!(isInValidId || isUnAccessed)) {
            Document document = Jsoup.parse(html);
            Element element_xml = document.select("div[id=webkit-xml-viewer-source-xml] *").first();
            Element element_setting = element_xml.select("Setting").first();
            baseUrl = "https://" + element_setting.attr("ProductionServer");
        } else {
            baseUrl = BASE;
        }
    }

    public PssUrl() {
        init();
        log.info("PssUrl初始化完成");
    }

}
