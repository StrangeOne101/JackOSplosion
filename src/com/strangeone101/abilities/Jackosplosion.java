package com.strangeone101.abilities;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.Material;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AvatarAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class Jackosplosion extends AvatarAbility implements AddonAbility {

	public static final String NAME = "JackOSplosion";
	public static final boolean SHOW_PUMPKINS_GLOBALLY = true;
	
	private static CopyOnWriteArrayList<Firework> fireworks = new CopyOnWriteArrayList<Firework>();
	
	private Location location;
	private Location startLocation;
	private Vector direction;
	private Random rand;
	
	private static Sound WITHER_SOUND;
	private static Sound SPIDER_SOUND;
	private static Sound WOLF_HOWL_SOUND;
	
	private ArmorStand pumpkinEntity;
	
	private static long cooldown = 3000L;
	private static int range = 25;
	private static double damage = 1.5D;
	
	public Jackosplosion(Player player) {
		super(player);

		this.location = player.getEyeLocation().add(0, -0.3, 0);
		this.startLocation = player.getEyeLocation().add(0, -0.3, 0);
		this.direction = player.getEyeLocation().getDirection().normalize();
		this.rand = new Random();
		this.player = player;
		
		this.pumpkinEntity = this.location.getWorld().spawn(this.location.clone().add(0, -1.8, 0), ArmorStand.class);
		this.pumpkinEntity.setVisible(false);
		this.pumpkinEntity.setCustomName(" ");
		this.pumpkinEntity.setMarker(true);
		this.pumpkinEntity.setGravity(false);
		this.pumpkinEntity.setAI(false);
		this.pumpkinEntity.setCollidable(false);
		this.pumpkinEntity.setSilent(true);
		
		this.pumpkinEntity.setVelocity(direction.multiply(10));
		//this.pumpkinEntity.setHeadPose(new EulerAngle(direction.getX(), direction.getY(), direction.getZ()));
		this.pumpkinEntity.setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
		
		
		bPlayer.addCooldown(this);
		
		start();
	}
	
	public static void affect(final LivingEntity entity, final Player... source) {
		for (int i = 0; i < 6; i++) {
			final int ii = i;
			new BukkitRunnable() {

				@Override
				public void run() {
					if (entity.isDead()) return;
					if (ii % 2 == 0) { //Every half second
						if (entity instanceof Player) {
							((Player)entity).playSound(entity.getLocation(), WOLF_HOWL_SOUND, 0.6F, 2F);
						}
						
						JackPacketHandler.sendHelmetUpdate(entity, new ItemStack(Material.CARVED_PUMPKIN), entity.getEntityId());
						if (source.length > 0) DamageHandler.damageEntity(entity, source[0], damage, CoreAbility.getAbility(NAME));
						else DamageHandler.damageEntity(entity, damage, CoreAbility.getAbility(NAME));
					} else {
						JackPacketHandler.sendHelmetUpdate(entity, entity.getEquipment().getHelmet(), entity.getEntityId());
					}

					ParticleEffect.BLOCK_CRACK.display(entity.getEyeLocation(), 10, 0.4D, 0.3D, 0.4D, 1, Material.PUMPKIN.createBlockData());
					ParticleEffect.BLOCK_CRACK.display(entity.getEyeLocation(), 10, 0.4D, 0.3D, 0.4D, 1, Material.BLACK_WOOL.createBlockData());
					//ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.PUMPKIN, (byte)2), 0.4F, 0.3F, 0.4F, 2F, 10, entity.getEyeLocation(), 128);
					//ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.WOOL, (byte)15), 0.4F, 0.3F, 0.4F, 2F, 10, entity.getEyeLocation(), 128);
				}
				
			}.runTaskLater(ProjectKorra.plugin, 10 * i);
		}
	}

	public void explode() {
		final Firework firework = this.location.getWorld().spawn(this.location.clone().add(this.direction.clone().multiply(-0.2)), Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.clearEffects();
		meta.setPower(5);
		firework.setTicksLived(1);
		meta.addEffect(FireworkEffect.builder().withColor(Color.RED, Color.BLACK, Color.ORANGE, Color.BLACK, Color.YELLOW).build());
		firework.setFireworkMeta(meta);
		firework.setSilent(true);
		
		fireworks.add(firework);
		
		final Firework f = firework;
		new BukkitRunnable() {

			@Override
			public void run() {
				f.detonate();
			}
		}.runTaskLater(ProjectKorra.plugin, 1L);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				fireworks.remove(firework);
			}
		}.runTaskLater(ProjectKorra.plugin, 4L);
		
		for (Entity e : GeneralMethods.getEntitiesAroundPoint(this.location, 2.5)) {
			if (e instanceof LivingEntity && e.getEntityId() != player.getEntityId() && e.getEntityId() != this.pumpkinEntity.getEntityId()) {
				DamageHandler.damageEntity(e, player, damage, this);
				affect((LivingEntity)e, player);
			}
		}
	}
	
	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void progress() {
		if (!bPlayer.canBendIgnoreCooldowns(this)) {
			remove();
			return;
		}
		
		if (rand.nextInt(5) == 0) {
			this.location.getWorld().playSound(location, SPIDER_SOUND, 1F, 1.9F);
		}
		
		if (rand.nextInt(5) == 0) {
			this.location.getWorld().playSound(location, WITHER_SOUND, 0.2F, 2F);
		}

		ParticleEffect.BLOCK_CRACK.display(this.location, 6, 0.4D, 0.3D, 0.4D, 1, Material.PUMPKIN.createBlockData());
		ParticleEffect.BLOCK_CRACK.display(this.location, 6, 0.4D, 0.3D, 0.4D, 1, Material.BLACK_WOOL.createBlockData());

		//ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.PUMPKIN, (byte)2), 0.5F, 0.5F, 0.5F, 1F, 6, location, 128);
		//ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.WOOL, (byte)15), 0.5F, 0.5F, 0.5F, 1F, 6, location, 128);
		
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(this.location, 1.8)) {
			if (entity instanceof ArmorStand && !((ArmorStand)entity).isVisible()) continue;
			
			if (entity instanceof LivingEntity && entity.getEntityId() != player.getEntityId() && entity.getEntityId() != pumpkinEntity.getEntityId()) {
				explode();
				remove();
				break;
			}
		}
		
		
		this.location.add(direction.clone().multiply(0.1));
		
		this.pumpkinEntity.teleport(this.location.clone().add(0, -1.8, 0));
		//this.pumpkinEntity.setHeadPose(new EulerAngle(direction.getX(), direction.getY(), direction.getZ()));
		
		if (GeneralMethods.isSolid(this.location.getBlock())) {
			explode();
			remove();
		}
		
		if (this.location.distance(this.startLocation) > range) {
			//player.sendMessage(range + " | " + this.location.distance(this.startLocation));
			explode();
			remove();
		}
	}
	
	@Override
	public void remove() {
		super.remove();
		
		this.pumpkinEntity.remove();
	}

	@Override
	public String getAuthor() {
		return "StrangeOne101";
	}

	@Override
	public String getVersion() {
		return "1.2 (2020)";
	}

	@Override
	public void load() {
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new JackListener(), ProjectKorra.plugin);
		
		ProjectKorra.log.info(NAME + " v" + getVersion() + " by " + getAuthor() + " enabled!");

		ConfigManager.defaultConfig.get().addDefault("ExtraAbilities.StrangeOne101." + NAME + ".Cooldown", cooldown);
		ConfigManager.defaultConfig.get().addDefault("ExtraAbilities.StrangeOne101." + NAME + ".Range", range);
		ConfigManager.defaultConfig.get().addDefault("ExtraAbilities.StrangeOne101." + NAME + ".Damage", damage);
		
		cooldown = ConfigManager.defaultConfig.get().getLong("ExtraAbilities.StrangeOne101." + NAME + ".Cooldown");
		range = ConfigManager.defaultConfig.get().getInt("ExtraAbilities.StrangeOne101." + NAME + ".Range");
		damage = ConfigManager.defaultConfig.get().getDouble("ExtraAbilities.StrangeOne101." + NAME + ".Damage");
		
		ConfigManager.defaultConfig.save();
		
		ConfigManager.languageConfig.get().addDefault("Abilities.Avatar." + NAME + ".DeathMessage", "{victim} was spoooked by {attacker}!");
		
		ConfigManager.languageConfig.save();
		
		WITHER_SOUND = Sound.ENTITY_WITHER_AMBIENT;
		SPIDER_SOUND = Sound.ENTITY_SPIDER_AMBIENT;
		WOLF_HOWL_SOUND = Sound.ENTITY_WOLF_HOWL;
		
		Permission perm = new Permission("bending.ability.JackOSplosion");
		if (Bukkit.getPluginManager().getPermission("bending.ability.JackOSplosion") == null) {
			Bukkit.getPluginManager().addPermission(perm);
			Bukkit.getPluginManager().getPermission("bending.ability.JackOSplosion").setDefault(PermissionDefault.TRUE);
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean requireAvatar() {
		return false;
	}
	
	@Override
	public String getDescription() {
		return "Happy Halloween! Spread the spook with this fancy new move by clicking and haunting your friends! (Click to fire, haunts hit players)";
	}
	
	public static boolean isJackOSplosion(Firework firework) {
		return fireworks.contains(firework);
	}

}
