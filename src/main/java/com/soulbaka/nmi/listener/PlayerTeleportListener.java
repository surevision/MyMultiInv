/*
 * This file is part of EssentialCmds, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 - 2015 HassanS6000
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.soulbaka.nmi.listener;

import com.soulbaka.nmi.NucleusMultiInv;
import com.soulbaka.nmi.util.Utils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

public class PlayerTeleportListener {

    @Listener
    public void onPlayerTeleport(MoveEntityEvent.Teleport event, @Getter("getTargetEntity") Player player)
    {
//        NucleusMultiInv.getNucleusMultiInv().getLogger()
//                .info("player " + player.getName() + " teleportes from: " + event.getFromTransform().getExtent().getName());
        if (!event.getFromTransform().getExtent().getUniqueId().equals(event.getToTransform().getExtent().getUniqueId()))
        {
            World oldWorld = event.getFromTransform().getExtent();
            World newWorld = event.getToTransform().getExtent();

            Utils.savePlayerInventory(player, oldWorld.getUniqueId());

//            NucleusMultiInv.getNucleusMultiInv().getLogger()
//                    .info("player " + player.getName() + " teleportes to: " + newWorld.getName());


            if (!Utils.doShareInventories(oldWorld.getName(), newWorld.getName()))
            {
                NucleusMultiInv.getNucleusMultiInv().getLogger()
                        .info("player " + player.getName() + " teleportes to new world : " + newWorld.getName());
                Utils.updatePlayerInventory(player, newWorld.getUniqueId());
            }

            player.offer(Keys.GAME_MODE, newWorld.getProperties().getGameMode());
        }
    }
}

