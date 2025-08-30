package cn.tqmjmc.tang.dreamchest.utils;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.config.TempConfig;
import cn.tqmjmc.tang.dreamchest.domain.SpawnChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Scheduler Utils Class
 * run certain task
 * concentrate on running tasks
 */
public class ScheUtils {

    // <spawnName, chestNumber>
    public static HashMap<String, Integer> map;

    public static HashMap<String, BukkitTask> tasks;

    public static void runCleanChest(){

        Bukkit.getScheduler().runTaskTimer(DreamChest.plugin, ()->{
            // prevent    Update Exception
            Set<Location> set = new HashSet<>(TempConfig.time.keySet());
            for (Location loc : set){
                long time = TempConfig.time.get(loc);
                time -= 10;
                // update
                TempConfig.addTime(loc, time);
                if (time <= 0) {
                    // delete
                    loc.getBlock().setType(Material.AIR);
                    TempConfig.removeLocation(loc);
                }
            }
        }, 10*20, 10*20);
    }

    public static void runSpawnChest(SpawnChest chest){
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(DreamChest.plugin, () -> {
            boolean b = SpawnUtils.spawnChest(chest);
            if (b) {
                int i = map.getOrDefault(chest.getSpawnName(), 0);
                i++;
                map.put(chest.getSpawnName(), i);
            }
        }, chest.getReTime() * 20, chest.getReTime() * 20);
        tasks.put(chest.getSpawnName(), task);
    }

}
