package cn.tqmjmc.tang.dreamchest.listener;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.DreamLang;
import cn.tqmjmc.tang.dreamchest.config.TempConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DreamListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (block != null && block.getType() == Material.CHEST) {
                boolean b = TempConfig.isRewardChest(block.getLocation());
                if (b) {
                    event.getPlayer().sendMessage(DreamLang.getOpenChest());
                    TempConfig.removeLocation(block.getLocation());
                    Bukkit.getScheduler().runTaskLater(DreamChest.plugin, ()->{
                        block.setType(Material.AIR);
                    }, 200);
                }
            }
        }

    }

}
