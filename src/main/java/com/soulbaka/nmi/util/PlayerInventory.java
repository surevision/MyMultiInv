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

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInventory
{
    private UUID playerUuid;
    private UUID worldUuid;
    private Map<String, ItemStack> slots;

    public PlayerInventory(UUID playerUuid, UUID worldUuid, Map<String, ItemStack> slots)
    {
        this.playerUuid = playerUuid;
        this.worldUuid = worldUuid;
        this.slots = slots;
    }

    public UUID getPlayerUuid()
    {
        return playerUuid;
    }

    public UUID getWorldUuid()
    {
        return worldUuid;
    }

    public Map<String, ItemStack> getSlots()
    {
        return slots;
    }
}
