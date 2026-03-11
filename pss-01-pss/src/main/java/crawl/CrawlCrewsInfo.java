package crawl;

import Do.CrewDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pojo.Enums.Collection;
import pojo.Enums.Rarity;
import pojo.Enums.Special;
import pojo.Equipments;
import utils.CrawlHtml;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import static utils.StringUtils.*;

/**
 * 该类的目标：爬取并解析指定的xml文件，将船员数据转储为本地csv文件
 */
@Log4j
public class CrawlCrewsInfo {

    /**
     * 爬取并解析 AllCharacterDesigns 页面，获取全部船员信息
     * @param url
     * @return
     */
    public List<CrewDo> parseCrewList(String url){
        log.info(">>>>> 正在爬取:" + url);
        List<CrewDo> crewDoList = new Vector<>();
        String html = CrawlHtml.getHtmlBySelenium(url);
        // 处理数据
        boolean isInValidId = html.matches("[\\s\\S]*Invalid[\\s\\S]*");
        boolean isUnAccessed = html.matches("[\\s\\S]*访问错误[\\s\\S]*");
        if (!(isInValidId || isUnAccessed)) {
            Document document = Jsoup.parse(html);
            Element element_xml = document.select("div[id=webkit-xml-viewer-source-xml] *").first();
            Elements elements_char = element_xml.select("CharacterDesign");
            log.info("全部船员数量："+elements_char.size());
            for (Element element_char : elements_char) {
                crewDoList.add(parseOneCrew(element_char));
            }
        } else {
            log.info(isInValidId+"|"+isUnAccessed);
        }
        return crewDoList;
    }

    /**
     * 解析一个船员数据，但只要 id + 中文名
     * @param element
     * @return
     */
    private CrewDo parseOneCrew4ZhName(Element element){
        CrewDo crewDo = new CrewDo();
        crewDo.setId(parseInt(element.attr("CharacterDesignId")));
        crewDo.setName_zh(element.attr("CharacterDesignName"));
        return crewDo;
    }

    /**
     * 解析一个船员的数据
     * @param element
     * @return
     */
    private CrewDo parseOneCrew(Element element) {
        CrewDo crewDo = new CrewDo();

        //分别拿到各项信息
        crewDo.setId(parseInt(element.attr("CharacterDesignId")));
        crewDo.setName_zh(element.attr("CharacterDesignName"));

        crewDo.setRarity(Rarity.valueOf(formatStr4Enum(element.attr("Rarity"))).name_zh);
        crewDo.setEquipments(Equipments.parseMask(parseInt(element.attr("EquipmentMask"))).toString());
        crewDo.setSpecial(Special.tranPssName(element.attr("SpecialAbilityType")).name_zh);
        crewDo.setCollection(Objects.requireNonNull(Collection.valueOf(parseInt(element.attr("CollectionDesignId")))).name_zh);

        crewDo.setHp(parseFloat(element.attr("FinalHp")));
        crewDo.setAttack(parseFloat(element.attr("FinalAttack")));
        crewDo.setRepair(parseFloat(element.attr("FinalRepair")));
        crewDo.setAbility(parseFloat(element.attr("SpecialAbilityFinalArgument")));

        crewDo.setScience(parseFloat(element.attr("FinalScience")));
        crewDo.setEngineer(parseFloat(element.attr("FinalEngine")));
        crewDo.setPilot(parseFloat(element.attr("FinalPilot")));
        crewDo.setWeapon(parseFloat(element.attr("FinalWeapon")));

        crewDo.setFire_resist(parseInt(element.attr("FireResistance")));
        crewDo.setWalk_speed(parseInt(element.attr("WalkingSpeed")));
        crewDo.setRun_speed(parseInt(element.attr("RunSpeed")));
        crewDo.setTraining(parseInt(element.attr("TrainingCapacity")));

        return crewDo;
    }

    public List<CrewDo> filterByRarity(List<CrewDo> source, Rarity rarity){
        return source.parallelStream()
                .filter(crewDo -> Objects.equals(crewDo.getRarity(), rarity.name_zh))
                .collect(Collectors.toList());
    }
}
