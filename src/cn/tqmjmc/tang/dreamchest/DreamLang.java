package cn.tqmjmc.tang.dreamchest;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DreamLang {

    private static String openChest;

    public static void load(){
        File lang = new File(DreamChest.dataFolder, "lang.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(lang);
        openChest = config.getString("openChest", "&a啵 你发现了个奖励箱！");
    }

    public static String getOpenChest() {
        return DreamChest.process(openChest);
    }
}
