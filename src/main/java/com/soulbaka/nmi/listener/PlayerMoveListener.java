package com.soulbaka.nmi.listener;

import com.soulbaka.nmi.NucleusMultiInv;
import com.soulbaka.nmi.util.Utils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

public class PlayerMoveListener {

    @Listener
    public void onPlayerMove(MoveEntityEvent event, @First Player player)
    {
        if (!event.getFromTransform().getExtent().getUniqueId().equals(event.getToTransform().getExtent().getUniqueId()))
        {
            World oldWorld = event.getFromTransform().getExtent();
            World newWorld = event.getToTransform().getExtent();

            Utils.savePlayerInventory(player, oldWorld.getUniqueId());

            NucleusMultiInv.getNucleusMultiInv().getLogger()
                    .info("player " + player.getName() + " moves " + newWorld.getName());


            if (!Utils.doShareInventories(oldWorld.getName(), newWorld.getName()))
            {
                NucleusMultiInv.getNucleusMultiInv().getLogger()
                        .info("player " + player.getName() + " moves to new world " + newWorld.getName());
                Utils.updatePlayerInventory(player, newWorld.getUniqueId());
            }

            player.offer(Keys.GAME_MODE, newWorld.getProperties().getGameMode());
        }
    }
}
