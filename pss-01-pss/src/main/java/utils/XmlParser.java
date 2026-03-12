package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pojo.CrewSpriteInfo;
import pojo.SpriteInfo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlParser {
    public static CrewSpriteInfo getCrewSpriteInfoFromCharacterDesign(String xmlPath, String crewId) {
        try {
            Document doc = parseXml(xmlPath);
            NodeList nodeList = doc.getElementsByTagName("CharacterDesign");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                if (elem.getAttribute("CharacterDesignId").equals(crewId)) {
                    Element partsElem = (Element) elem.getElementsByTagName("CharacterParts").item(0);
                    NodeList partList = partsElem.getElementsByTagName("CharacterPart");
                    String head = null, body = null, leg = null;
                    for (int j = 0; j < partList.getLength(); j++) {
                        Element part = (Element) partList.item(j);
                        String type = part.getAttribute("CharacterPartType");
                        String spriteId = part.getAttribute("StandardSpriteId");
                        switch (type) {
                            case "Head":
                                head = spriteId;
                                break;
                            case "Body":
                                body = spriteId;
                                break;
                            case "Leg":
                                leg = spriteId;
                                break;
                        }
                    }
                    if (head != null && body != null && leg != null) {
                        return new CrewSpriteInfo(head, body, leg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SpriteInfo getSpriteInfoFromSprites(String xmlPath, String spriteId) {
        try {
            Document doc = parseXml(xmlPath);
            NodeList nodeList = doc.getElementsByTagName("Sprite");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                if (elem.getAttribute("SpriteId").equals(spriteId)) {
                    return new SpriteInfo(
                            elem.getAttribute("ImageFileId"),
                            elem.getAttribute("X"),
                            elem.getAttribute("Y"),
                            elem.getAttribute("Width"),
                            elem.getAttribute("Height")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Document parseXml(String xmlPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(xmlPath));
    }
}
