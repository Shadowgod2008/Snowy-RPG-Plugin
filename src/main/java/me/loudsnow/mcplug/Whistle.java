package me.loudsnow.mcplug;


import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.loudsnow.mcplug.Main.*;

public class Whistle implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (!cd7.containsKey(p.getUniqueId().toString())) {

            if (!horsealive.containsKey(p.getUniqueId().toString())) {
                if (horseowner.containsKey(p.getUniqueId().toString())) {
                    Horse horse = (Horse) p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
                    horse.setCustomNameVisible(true);
                    horse.setCustomName("" + ChatColor.GOLD + ChatColor.BOLD + p.getName() + "'s " + ChatColor.YELLOW + ChatColor.BOLD + "horse");
                    horse.setOwner(p);
                    horsealive.put(p.getUniqueId().toString(), 0);
                    horse.isAdult();
                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                    cd7.put(p.getUniqueId().toString(), 0);
                    horse.setColor(Horse.Color.WHITE);
                    horse.setTarget(p);
                    horse.setJumpStrength(0.8);
                    horse.setHealth(14);
                    horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
                    horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(14);
                    Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                        @Override
                        public void run() {
                            cd7.remove(p.getUniqueId().toString());
                            p.sendMessage(ChatColor.GREEN + "Your whistle is no longer on cooldown!");
                        }
                    }, 600);
                } else {
                    p.sendMessage(ChatColor.RED + "You don't own a horse!");
                }
            } else {
                for (World w : Bukkit.getWorlds()) {
                    for (Entity entity : w.getEntities()) {
                        if (entity instanceof Horse) {
                            if (((Horse) entity).getOwner() != null) {
                                if (((Horse) entity).getOwner() == p) {
                                    double locX = entity.getLocation().getX();
                                    double locY = entity.getLocation().getY();
                                    double locZ = entity.getLocation().getZ();

                                    entity.teleport(p.getLocation());
                                    p.sendMessage(ChatColor.GREEN + "Summoning Horse!");
                                    horsealive.put(p.getUniqueId().toString(), 0);
                                    cd7.put(p.getUniqueId().toString(), 0);
                                    new Location(Bukkit.getWorld("spigot_1_18_1_1651099"), locX, locY, locZ).getChunk().unload();
                                    Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                                        @Override
                                        public void run() {
                                            cd7.remove(p.getUniqueId().toString());
                                            p.sendMessage(ChatColor.GREEN + "Your whistle is no longer on cooldown!");

                                        }
                                    }, 600);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "You need to wait 30 seconds between use!");
        }
        return true;
    }

}
