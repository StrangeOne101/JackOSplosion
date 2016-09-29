package com.strangeone101.abilities.nms;

import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_9_R1.EnumItemSlot;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityEquipment;

public class NMS1_9RC1 {
	
	public static void sendHelmetUpdate(Player player, ItemStack item, int id) {
		net.minecraft.server.v1_9_R1.ItemStack item2 = CraftItemStack.asNMSCopy(item);
		EnumItemSlot slot = EnumItemSlot.HEAD;
		PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(id, slot, item2);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
}
