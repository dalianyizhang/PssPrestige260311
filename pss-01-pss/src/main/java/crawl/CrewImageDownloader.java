package crawl;

import Do.CrewDo;
import lombok.extern.log4j.Log4j;
import pojo.CrewSpriteInfo;
import pojo.Enums.Rarity;
import pojo.SpriteInfo;
import utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

@Log4j
public class CrewImageDownloader {
    private final String baseDir;

    public CrewImageDownloader(String baseDir) {
        this.baseDir = baseDir;
        // 创建必要的目录
        FileUtils.createDirectory(baseDir + "/xml");
        FileUtils.createDirectory(baseDir + "/img/amazon");
        FileUtils.createDirectory(baseDir + "/img/crew");
    }

    /**
     * 获取船员图片，若本地缓存已存在则直接返回，否则下载并合成
     *
     * @param crewId 船员ID
     * @return 合成后的图片文件路径，失败返回null
     */
    public String getCrewImage(String crewId) {
        String crewCachePath = baseDir + "/img/crew/" + crewId + ".png";
        if (FileUtils.fileExists(crewCachePath)) {
            System.out.println("使用缓存图片: " + crewCachePath);
            return crewCachePath;
        }

        // 1. 确保XML文件存在
        String characterXml = XmlFileManager.ensureListAllCharacterDesigns2Xml(baseDir);
        String spritesXml = XmlFileManager.ensureListSpritesXml(baseDir);
        if (characterXml == null || spritesXml == null) {
            System.err.println("无法获取必要的XML文件");
            return null;
        }

        // 2. 获取船员三部分SpriteId
        CrewSpriteInfo crewSpriteInfo = XmlParser.getCrewSpriteInfoFromCharacterDesign(characterXml, crewId);
        if (crewSpriteInfo == null) {
            System.err.println("未找到船员信息，ID: " + crewId);
            return null;
        }

        // 3. 获取各部分SpriteInfo并下载/裁剪
        SpriteInfo headInfo = crewSpriteInfo.headSpriteId != null ?
                XmlParser.getSpriteInfoFromSprites(spritesXml, crewSpriteInfo.headSpriteId) : null;
        SpriteInfo bodyInfo = crewSpriteInfo.bodySpriteId != null ?
                XmlParser.getSpriteInfoFromSprites(spritesXml, crewSpriteInfo.bodySpriteId) : null;
        SpriteInfo legInfo = crewSpriteInfo.legSpriteId != null ?
                XmlParser.getSpriteInfoFromSprites(spritesXml, crewSpriteInfo.legSpriteId) : null;

        BufferedImage head = downloadAndCrop(headInfo);
        BufferedImage body = downloadAndCrop(bodyInfo);
        BufferedImage leg = downloadAndCrop(legInfo);

        // 4. 合成
        BufferedImage combined = combineParts(head, body, leg);
        if (combined == null) {
            System.err.println("合成图片失败");
            return null;
        }

        // 5. 保存
        boolean saved = ImageProcessor.saveImage(combined, crewCachePath);
        if (saved) {
            System.out.println("船员图片已保存: " + crewCachePath);
            return crewCachePath;
        } else {
            System.err.println("保存图片失败");
            return null;
        }
    }

    /**
     * 根据SpriteInfo下载原始图片（若不存在）并裁剪
     */
    private BufferedImage downloadAndCrop(SpriteInfo info) {
        if (info == null) return null;

        String amazonPath = baseDir + "/img/amazon/" + info.imageFileId + ".png";
        if (!FileUtils.fileExists(amazonPath)) {
            String url = "https://pixelstarships.s3.amazonaws.com/" + info.imageFileId + ".png";
            boolean ok = NetworkUtils.downloadFile(url, amazonPath);
            if (!ok) {
                System.err.println("下载原始图片失败: " + info.imageFileId);
                return null;
            }
        }

        try {
            int x = Integer.parseInt(info.x);
            int y = Integer.parseInt(info.y);
            int w = Integer.parseInt(info.width);
            int h = Integer.parseInt(info.height);
            return ImageProcessor.cropImage(amazonPath, x, y, w, h);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将三部分图片垂直居中合成
     */
    private BufferedImage combineParts(BufferedImage head, BufferedImage body, BufferedImage leg) {
        if (head == null && body == null && leg == null) return null;

        int maxWidth = 0;
        int totalHeight = 0;
        if (head != null) {
            maxWidth = Math.max(maxWidth, head.getWidth());
            totalHeight += head.getHeight();
        }
        if (body != null) {
            maxWidth = Math.max(maxWidth, body.getWidth());
            totalHeight += body.getHeight();
        }
        if (leg != null) {
            maxWidth = Math.max(maxWidth, leg.getWidth());
            totalHeight += leg.getHeight();
        }

        if (maxWidth <= 0 || totalHeight <= 0) return null;

        BufferedImage result = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        int y = 0;
        if (head != null) {
            int x = (maxWidth - head.getWidth()) / 2;
            g.drawImage(head, x, y, null);
            y += head.getHeight();
        }
        if (body != null) {
            int x = (maxWidth - body.getWidth()) / 2;
            g.drawImage(body, x, y, null);
            y += body.getHeight();
        }
        if (leg != null) {
            int x = (maxWidth - leg.getWidth()) / 2;
            g.drawImage(leg, x, y, null);
        }
        g.dispose();
        return result;
    }


    public static void main(String[] args) {
        String outputPath = "downloads";

        String baseUrl = new PssUrl().getBaseUrl();
        String crewUrl = String.format(PssUrl.CREW_ALL, baseUrl);
        CrawlCrewsInfo crawlCrews = new CrawlCrewsInfo();
        CrawlPrestigeInfo crawlPrestige = new CrawlPrestigeInfo();

        // 留存最后结果=0的id，便于稍后尝试重新爬取
        java.util.List<Integer> preTo = new Vector<>();
        java.util.List<Integer> preFrom = new Vector<>();


        // 1.爬取船员基本信息
        // 1.1 原始数据
        java.util.List<CrewDo> crewDoList = crawlCrews.parseCrewList(crewUrl);
        // 1.2 数据清洗 去除机器人
        crewDoList = crewDoList.parallelStream()
                .filter(crewDo -> !CrawlPrestigeInfo.filter.contains(crewDo.getId()))
                .collect(Collectors.toCollection(Vector::new));
        // 1.3 统一存储
        java.util.List<String[]> crews = crewDoList.parallelStream()
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

        // 1.5 下载并处理所有船员的图片
        String sourceDir = outputPath + "/source";
        CrewImageDownloader downloader = new CrewImageDownloader(sourceDir);
        // 先下载好 XML文件
        String characterXml = XmlFileManager.ensureListAllCharacterDesigns2Xml(sourceDir);
        String spritesXml = XmlFileManager.ensureListSpritesXml(sourceDir);
        crewDoList.parallelStream().forEach(crewDo -> {
            downloader.getCrewImage(String.valueOf(crewDo.getId()));
        });
        try {
            // 移动 crew 目录到 downloads 根目录
            Path sourceCrew = Paths.get(sourceDir, "img", "crew");
            Path destCrew = Paths.get(outputPath, "crew");
            if (Files.exists(sourceCrew)) {
                // 若目标 crew 已存在，先删除（递归）
                if (Files.exists(destCrew)) {
                    FileUtils.deleteDirectory(destCrew);  // 需要实现该方法
                }
                Files.move(sourceCrew, destCrew);
                log.info("已移动 crew 目录到: " + destCrew);
            }

            Path sourceDirPath = Paths.get(sourceDir);
            if (Files.exists(sourceDirPath)) {
                FileUtils.deleteDirectory(sourceDirPath);
                log.info("已删除中间目录: " + sourceDir);
            }
        } catch (IOException e) {
            log.error("清理中间文件失败", e);
        }

    }
}
