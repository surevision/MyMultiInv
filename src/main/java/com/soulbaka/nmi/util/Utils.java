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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.soulbaka.nmi.NucleusMultiInv;
import com.soulbaka.nmi.config.Config;
import com.soulbaka.nmi.config.Configurable;
import com.soulbaka.nmi.config.InventoryConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.function.BiConsumer;

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
                    stack = player.getBoots();
                else if (i == 37)
                stack = player.getLeggings();
                else if (i == 38)
                    stack = player.getChestplate();
                else if (i == 39)
                    stack = player.getHelmet();
                else if (i == 40)
                    stack = player.	getItemInHand(HandTypes.OFF_HAND);
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
        Map<String, ItemStack> slots = Maps.newTreeMap();
        Iterator<Object> iterator = parentNode.getChildrenMap().keySet().iterator();
        for (int slot = 0; slot < parentNode.getChildrenMap().keySet().size(); slot++)
        {
//            NucleusMultiInv.getNucleusMultiInv().getLogger().info("config slot " + slot);
            Object obj = iterator.next();
//            NucleusMultiInv.getNucleusMultiInv().getLogger().info("key slot " + obj.toString());
            if (parentNode.getChildrenMap().keySet().contains(obj))
            {
                ConfigurationNode inventoryNode = Configs.getConfig(inventoryConfig).getNode("inventory", playerUuid.toString(), worldUuid.toString(), "slots", String.valueOf(obj));
                Optional<ItemStack> optionalStack = ItemStackSerializer.readItemStack(inventoryNode, obj);
                slots.put(String.valueOf(obj), optionalStack.orElse(ItemStack.builder().itemType(ItemTypes.NONE).build()));
            }
            else
            {
                slots.put(String.valueOf(obj), ItemStack.builder().itemType(ItemTypes.NONE).build());
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
            Utils.forEach(player.getInventory().slots(), (index, c) -> {
                if (playerInventory.getSlots().get(String.valueOf(index)) != null) {
                    ((Inventory)c).set(playerInventory.getSlots().get(String.valueOf(index)));
                }
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

    public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);

        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}
