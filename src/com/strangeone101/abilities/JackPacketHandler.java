package com.strangeone101.abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.projectkorra.projectkorra.ProjectKorra;
import com.strangeone101.abilities.nms.NMS1_10RC1;
import com.strangeone101.abilities.nms.NMS1_9RC1;
import com.strangeone101.abilities.nms.NMS1_9RC2;

public class JackPacketHandler {

	protected static String nms = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
	
	public static void sendHelmetUpdate(LivingEntity player, ItemStack item, int entityID) {
		if (nms.equals("v1_9_R1")) {
			if (Jackosplosion.SHOW_PUMPKINS_GLOBALLY) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getWorld().getName().equals(player.getWorld().getName())) {
						NMS1_9RC1.sendHelmetUpdate(p, item, entityID); 
					}
				}
			} else if (player instanceof Player) {
				NMS1_9RC1.sendHelmetUpdate((Player) player, item, entityID); 
			}
		} else if (nms.equals("v1_9_R2")) {
			if (Jackosplosion.SHOW_PUMPKINS_GLOBALLY) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getWorld().getName().equals(player.getWorld().getName())) {
						NMS1_9RC2.sendHelmetUpdate(p, item, entityID); 
					}
				}
			} else if (player instanceof Player) {
				NMS1_9RC2.sendHelmetUpdate((Player) player, item, entityID); 
			}
		} else if (nms.equals("v1_10_R1")) {
			if (Jackosplosion.SHOW_PUMPKINS_GLOBALLY) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getWorld().getName().equals(player.getWorld().getName())) {
						NMS1_10RC1.sendHelmetUpdate(p, item, entityID); 
					}
				}
			} else if (player instanceof Player) {
				NMS1_10RC1.sendHelmetUpdate((Player) player, item, entityID); 
			}
		} else {
			ProjectKorra.log.warning("Tried to send armor packet to player but version is wrong! " + nms);
		}
	}
}
