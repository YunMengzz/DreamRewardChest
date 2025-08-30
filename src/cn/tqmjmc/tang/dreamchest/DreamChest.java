package cn.tqmjmc.tang.dreamchest;

import cn.tqmjmc.tang.dreamchest.config.DreamConfig;
import cn.tqmjmc.tang.dreamchest.config.TempConfig;
import cn.tqmjmc.tang.dreamchest.domain.RewardChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnArea;
import cn.tqmjmc.tang.dreamchest.listener.DreamListener;
import cn.tqmjmc.tang.dreamchest.utils.ScheUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;


public class DreamChest extends JavaPlugin {

    public static DreamChest plugin;
    public static Configuration config;
    public static Configuration lang;
    public static File dataFolder;


    @Override
    public void onEnable() {
        if (plugin != null) {
            this.getLogger().info("请勿启动两次本插件!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        dataFolder = getDataFolder();
        ScheUtils.map = new HashMap<>();

        // 注册Listener和Serialization
        ConfigurationSerialization.registerClass(SpawnArea.class);
        ConfigurationSerialization.registerClass(SpawnChest.class);
        ConfigurationSerialization.registerClass(RewardChest.class);

        Bukkit.getPluginManager().registerEvents(new DreamListener(), this);

        saveDefaultConfig();
        saveResource("lang.yml", false);
        DreamConfig.load();
        ScheUtils.runCleanChest();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        TempConfig.disable();
        plugin = null;
    }

    public static String process(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length == 0 || "help".equalsIgnoreCase(args[0])) {

        }
        return true;
    }

    public static void help (CommandSender sender){

    }
}
