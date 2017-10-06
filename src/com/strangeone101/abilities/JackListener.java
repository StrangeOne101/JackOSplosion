package com.strangeone101.abilities;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

public class JackListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerAnimationEvent event) {
		if (event.isCancelled()) return;
		
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
		if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility(Jackosplosion.NAME))) {
			new Jackosplosion(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) return;
		
		if (event.getDamager() instanceof Firework && Jackosplosion.isJackOSplosion((Firework) event.getDamager())) {
			event.setCancelled(true);
		}
	}
}
