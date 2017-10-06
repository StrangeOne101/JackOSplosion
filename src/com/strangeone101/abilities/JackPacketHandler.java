package com.strangeone101.abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.strangeone101.abilities.nms.NMS_Other;

public class JackPacketHandler {

	protected static String nms = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
	
	public static void sendHelmetUpdate(LivingEntity player, ItemStack item, int entityID) {
		if (Jackosplosion.SHOW_PUMPKINS_GLOBALLY) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getWorld().getName().equals(player.getWorld().getName())) {
					NMS_Other.sendHelmetUpdateRF(p, item, entityID); 
				}
			}
		} else if (player instanceof Player) {
			NMS_Other.sendHelmetUpdateRF((Player) player, item, entityID); 
		}
	}
}
