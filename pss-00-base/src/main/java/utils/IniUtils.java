package utils;

import lombok.extern.log4j.Log4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Log4j
public class IniUtils {

    public static void updateValue(File iniFile, String sectionToModify, String keyToModify, String newValue) {
        // 用于存储整个INI文件内容的Map
        Map<String, Map<String, String>> iniContent = null;
        try {
            iniContent = readIniFile(iniFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }

        // 修改键值对
        if (iniContent.containsKey(sectionToModify)) {
            Map<String, String> section = iniContent.get(sectionToModify);
            section.put(keyToModify, newValue);
        } else {
            Map<String, String> section = new HashMap<>();
            section.put(keyToModify, newValue);
            iniContent.put(sectionToModify, section);
        }

        // 将修改后的内容写回文件
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(iniFile))) {
            for (Map.Entry<String, Map<String, String>> sectionEntry : iniContent.entrySet()) {
                bw.write("[" + sectionEntry.getKey() + "]");
                bw.newLine();
                Map<String, String> section = sectionEntry.getValue();
                for (Map.Entry<String, String> keyValueEntry : section.entrySet()) {
                    bw.write(keyValueEntry.getKey() + "=" + keyValueEntry.getValue());
                    bw.newLine();
                }
                bw.newLine(); // 在每个节之后添加一个空行
            }
            bw.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        // log.info("已经写好");
    }

    public static String readValue(File file, String key) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // 忽略空行和注释行（假设注释以#或;开头）
            if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                continue;
            }

            // 检查是否是节标题（用方括号括起来）
            if (line.startsWith("[") && line.endsWith("]")) {
                continue;
            }

            if (line.contains("=")) {
                // 否则，这应该是一个键值对
                int equalsIndex = line.indexOf('=');
                if (equalsIndex != -1) {
                    if (line.substring(0, equalsIndex).trim().equals(key)) {
                        return line.substring(equalsIndex + 1).trim();
                    }
                }
            }
        }
        reader.close();
        return null;
    }

    public static Map<String, Map<String, String>> readIniFile(File file) throws IOException {
        Map<String, Map<String, String>> iniData = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        String currentSection = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // 忽略空行和注释行（假设注释以#或;开头）
            if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                continue;
            }

            // 检查是否是节标题（用方括号括起来）
            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1).trim();
                iniData.put(currentSection, new HashMap<>());
            } else if (currentSection != null) {
                // 否则，这应该是一个键值对
                int equalsIndex = line.indexOf('=');
                if (equalsIndex != -1) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    iniData.get(currentSection).put(key, value);
                }
            }
        }
        reader.close();
        return iniData;
    }
}