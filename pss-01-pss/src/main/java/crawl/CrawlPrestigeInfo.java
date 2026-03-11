package crawl;

import Do.PrestigeDo;
import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.CrawlHtml;

import java.util.*;

import static utils.StringUtils.parseInt;

@Log4j
public class CrawlPrestigeInfo {

    public static final Set<Integer> filter = new HashSet<>(Arrays.asList(17, 155, 166, 168, 169, 170, 171, 172, 173,
            174, 175, 176, 177, 178, 179, 180, 181, 200, 201, 214, 339, 353, 389, 390, 391, 426));

    /**
     * 爬取并解析指定url中的所有合成数据
     *
     * @param url
     * @return
     */
    public List<PrestigeDo> parsePrestigeList(String url) {
        log.info(">>>>> 正在爬取:" + url);
        List<PrestigeDo> prestigeDoList = new Vector<>();
        String html = CrawlHtml.getHtmlBySelenium(url);
        // log.info("目标网页大小：" + html.length());
        // log.info(html);
        try {
            // 处理数据
            boolean isInValidId = html.matches("[\\s\\S]*Invalid[\\s\\S]*");
            boolean isUnAccessed = html.matches("[\\s\\S]*访问错误[\\s\\S]*");
            if (!(isInValidId || isUnAccessed)) {
                Document document = Jsoup.parse(html);
                Element element_xml = document.select("div[id=webkit-xml-viewer-source-xml] *").first();
                Elements elements_pres = element_xml.select("Prestige");
                // log.info("处理前："+elements_pres.size());
                if (elements_pres.size() > 0) {
                    boolean isPrestigeTo = url.matches(".*PrestigeCharacterTo.*");
                    for (Element element_pres : elements_pres) {
                        PrestigeDo prestigeDo = new PrestigeDo();
                        prestigeDo.setFirstSonId(parseInt(element_pres.attr("CharacterDesignId1")));
                        prestigeDo.setSecondSonId(parseInt(element_pres.attr("CharacterDesignId2")));
                        prestigeDo.setFatherId(parseInt(element_pres.attr("ToCharacterDesignId")));
                        // 1.这里进行了第一次数据清洗，去除了prestigeTo结果集中的重复数据（例如：5+2 和 2+5，只保留后者）
                        if(!isPrestigeTo) {
                            prestigeDoList.add(prestigeDo);
                        } else if(prestigeDo.getFirstSonId() <= prestigeDo.getSecondSonId()) {
                            prestigeDoList.add(prestigeDo);
                        }
                    }
                }
                // log.info("第一次处理后的条数："+ prestigeDoList.size());
            }
            // log.info("<<<<<<<<<爬取完成" + url);
        } catch (NullPointerException e) {
            // 递归式的爬取，直到爬取成功 // 危险操作，不建议启用
            // return parsePrestigeList(url);
            log.error(url + ":" + e);
        }
        // 2.这里进行了第二次数据清洗，去除了包含机器人的合成记录
        List<PrestigeDo> prestigeDoList1 = dataClean(prestigeDoList);
        log.info("处理后的条数：" + prestigeDoList1.size());
        return prestigeDoList1;
    }

    /**
     * 数据清洗，清除机器人数据
     *
     * @param source
     * @return
     */
    private List<PrestigeDo> dataClean(List<PrestigeDo> source) {
        // 空集处理
        List<PrestigeDo> res = new Vector<>();
        if (source == null || source.size() == 0) {
            return res;
        }
        // 若记录的s1/s2/f任一在提前准备机器人id表中，则跳过
        // ！！！注意：随着版本迭代，可能有新增的机器人，则需要检查crew_all表进行对照，并手动更新本类中的哈希表
        source.parallelStream().forEach(prestigeDo -> {
            boolean hasId = filter.contains(prestigeDo.getFirstSonId())
                    || filter.contains(prestigeDo.getSecondSonId())
                    || filter.contains(prestigeDo.getFatherId());
            if (!hasId) {
                res.add(prestigeDo);
            }
        });
        return res;
    }

}
