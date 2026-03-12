package utils;

public class XmlFileManager {
    private static final String BASE_URL = "https://api.pixelstarships.com";

    public static String ensureListAllCharacterDesigns2Xml(String baseDir) {
        String path = baseDir + "/xml/ListAllCharacterDesigns2.xml";
        if (!FileUtils.fileExists(path)) {
            String url = BASE_URL + "/CharacterService/ListAllCharacterDesigns2?languageKey=zh-CN";
            boolean ok = NetworkUtils.downloadFile(url, path);
            if (!ok) return null;
        }
        return path;
    }

    public static String ensureListSpritesXml(String baseDir) {
        String path = baseDir + "/xml/ListSprites.xml";
        if (!FileUtils.fileExists(path)) {
            String url = BASE_URL + "/FileService/ListSprites";
            boolean ok = NetworkUtils.downloadFile(url, path);
            if (!ok) return null;
        }
        return path;
    }
}