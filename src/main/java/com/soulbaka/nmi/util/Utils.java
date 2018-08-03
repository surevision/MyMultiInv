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
package com.soulbaka.nmi.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.soulbaka.nmi.NucleusMultiInv;
import com.soulbaka.nmi.config.Config;
import com.soulbaka.nmi.config.Configurable;
import com.soulbaka.nmi.config.InventoryConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Utils {

    private static Configurable mainConfig = Config.getConfig();
    private static Configurable inventoryConfig = InventoryConfig.getConfig();
    public static void savePlayerInventory(Player player, UUID worldUuid)
    {
        if (!Utils.areSeparateWorldInventoriesEnabled())
            return;

        String worldName = Sponge.getServer().getWorld(worldUuid).get().getName();
        Set<UUID> worlds = Sets.newHashSet();

        for (World world : Sponge.getServer().getWorlds())
        {
            if (Utils.doShareInventories(worldName, world.getName()))
            {
                if (!worlds.contains(world.getUniqueId()))
                    worlds.add(world.getUniqueId());
            }
        }

        if (!worlds.contains(worldUuid))
            worlds.add(worldUuid);

        for (UUID uuid : worlds)
        {
            List<Inventory> slots = Lists.newArrayList(player.getInventory().slots());

            for (int i = 0; i < slots.size(); i++)
            {
                Optional<ItemStack> stack;

                if (i < 36)
                    stack = slots.get(i).peek();
                else if (i == 36)
                    stack = player.getHelmet();
                else if (i == 37)
                    stack = player.getChestplate();
                else if (i == 38)
                    stack = player.getLeggings();
                else if (i == 39)
                    stack = player.getBoots();
                else
                    stack = Optional.empty();

                if (stack.isPresent())
                {
                    Object object = ItemStackSerializer.serializeItemStack(stack.get());
                    Configs.setValue(inventoryConfig, new Object[] { "inventory", player.getUniqueId().toString(), uuid.toString(), "slots", String.valueOf(i) }, object);
                }
                else if (Configs.getConfig(inventoryConfig).getNode("inventory", player.getUniqueId().toString(), uuid.toString(), "slots").getChildrenMap().containsKey(String.valueOf(i)))
                {
                    Configs.removeChild(inventoryConfig, new Object[] { "inventory", player.getUniqueId().toString(), uuid.toString(), "slots" }, String.valueOf(i));
                }
            }
        }
    }

    public static PlayerInventory getPlayerInventory(UUID playerUuid, UUID worldUuid)
    {
        ConfigurationNode parentNode = Configs.getConfig(inventoryConfig).getNode("inventory", playerUuid.toString(), worldUuid.toString(), "slots");
        List<ItemStack> slots = Lists.newArrayList();

        for (int slot = 0; slot < parentNode.getChildrenMap().keySet().size(); slot++)
        {
            NucleusMultiInv.getNucleusMultiInv().getLogger().info("config slot " + slot);
            if (parentNode.getChildrenMap().keySet().contains(String.valueOf(slot)))
            {
                ConfigurationNode inventoryNode = Configs.getConfig(inventoryConfig).getNode("inventory", playerUuid.toString(), worldUuid.toString(), "slots", String.valueOf(slot));
                Optional<ItemStack> optionalStack = ItemStackSerializer.readItemStack(inventoryNode, slot);
                slots.add(optionalStack.orElse(ItemStack.builder().itemType(ItemTypes.NONE).build()));
            }
            else
            {
                slots.add(ItemStack.builder().itemType(ItemTypes.NONE).build());
            }
        }

        return new PlayerInventory(playerUuid, worldUuid, slots);
    }

    public static void updatePlayerInventory(Player player, UUID worldUuid)
    {
        if (!Utils.areSeparateWorldInventoriesEnabled())
            return;

        PlayerInventory playerInventory = Utils.getPlayerInventory(player.getUniqueId(), worldUuid);
        player.getInventory().clear();

        if (playerInventory != null)
        {
            final Iterator<ItemStack> slots = playerInventory.getSlots().iterator();
            player.getInventory().slots().forEach(c -> {
                if (slots.hasNext())
                    c.set(slots.next());
            });
        }
    }

    public static boolean areSeparateWorldInventoriesEnabled()
    {
        return Configs.getConfig(mainConfig).getNode("world", "inventory", "separate").getBoolean();
    }

    public static boolean doShareInventories(String world1, String world2)
    {
        CommentedConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode("world", "inventory", "groups");

        for (Object group : valueNode.getChildrenMap().keySet())
        {
            String worldString = Configs.getConfig(mainConfig).getNode("world", "inventory", "groups", String.valueOf(group)).getString();
            List<String> worlds = Arrays.asList(worldString.split("\\s*,\\s*"));

            if (worlds.contains(world1) && worlds.contains(world2))
                return true;
        }

        return false;
    }

    public static void doReload()
    {
        mainConfig.setup();
    }
}
