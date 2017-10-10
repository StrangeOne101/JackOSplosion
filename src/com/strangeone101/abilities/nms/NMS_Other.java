package com.strangeone101.abilities.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.projectkorra.projectkorra.util.ReflectionHandler;
import com.projectkorra.projectkorra.util.ReflectionHandler.PackageType;

public class NMS_Other {
	public static void sendHelmetUpdateRF(Player player, ItemStack item, int id) {
		try {
			Method asNMSCopy = ReflectionHandler.getMethod("CraftItemStack", PackageType.CRAFTBUKKIT_INVENTORY, "asNMSCopy", ItemStack.class);
			Field slotField = ReflectionHandler.getField("EnumItemSlot", PackageType.MINECRAFT_SERVER, true, "HEAD");
			Object itemCopy = asNMSCopy.invoke(null, item);
			Constructor<?> packetConstructor = ReflectionHandler.getConstructor("PacketPlayOutEntityEquipment", PackageType.MINECRAFT_SERVER, Integer.TYPE, slotField.getType(), itemCopy.getClass());
			Object packet = packetConstructor.newInstance(id, slotField.get(null), itemCopy);
			Method getHandle = ReflectionHandler.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
			Object handlePlayer = getHandle.invoke(player);
			Object playerConn = ReflectionHandler.getField(handlePlayer.getClass(), true, "playerConnection").get(handlePlayer);
			Method sendPacket = ReflectionHandler.getMethod(playerConn.getClass(), "sendPacket", packet.getClass());
			sendPacket.invoke(playerConn, packet);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
