package me.mattyhd0.koth.creator.selection;

import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class KothSelectionListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){

        Player player = event.getPlayer();

        if(player.hasPermission("koth.selection") && player.getItemInHand().equals(KothSelectionWand.getWand())){

            Action action = event.getAction();
            if(event.getClickedBlock() != null) {

                Location blockLocation = event.getClickedBlock().getLocation();

                if(action == Action.LEFT_CLICK_BLOCK){

                    event.setCancelled(true);
                    KothSelection.setPos1(player, blockLocation.add(1, 0, 0));
                    player.sendMessage(
                            Util.color(
                                    Config.getMessage("pos1-selected")
                                            .replaceAll("\\{world}", blockLocation.getWorld().getName())
                                            .replaceAll("\\{x}", blockLocation.getX()+"")
                                            .replaceAll("\\{y}", blockLocation.getY()+"")
                                            .replaceAll("\\{z}", blockLocation.getZ()+"")

                            )
                    );

                } else if (action == Action.RIGHT_CLICK_BLOCK){

                    event.setCancelled(true);
                    KothSelection.setPos2(player, blockLocation.add(1, 0, 0));
                    player.sendMessage(
                            Util.color(
                                    Config.getMessage("pos2-selected")
                                            .replaceAll("\\{world}", blockLocation.getWorld().getName())
                                            .replaceAll("\\{x}", blockLocation.getX()+"")
                                            .replaceAll("\\{y}", blockLocation.getY()+"")
                                            .replaceAll("\\{z}", blockLocation.getZ()+"")

                            )
                    );

                }

            }

        }

    }

}
