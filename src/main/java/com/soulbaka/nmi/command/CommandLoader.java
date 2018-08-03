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
package com.soulbaka.nmi.command;

import com.google.common.collect.Sets;
import com.soulbaka.nmi.NucleusMultiInv;
import org.spongepowered.api.Sponge;

import java.util.Set;

/**
 * Utility class that handles loading and registering commands.
 */
public final class CommandLoader {

    /* (non-Javadoc)
     *
     * No need to instantiate!
     */
    private CommandLoader() {}

    private static Set<? extends CommandExecutorBase> getCommands() {
        return Sets.newHashSet(
            new ReloadExecutor()
        );
    }

    /**
     * Registers the EssentialCmds commands.
     */
    public static void registerCommands() {
        // TODO: Put module checks here in a stream.
        // getCommands().stream().filter(c -> c.getAssociatedModules().length == 0 && checkModules).forEach(CommandLoader::registerCommand);
        getCommands().forEach(cmd -> Sponge.getCommandManager().register(NucleusMultiInv.getNucleusMultiInv(), cmd.getSpec(), cmd.getAliases()));
    }
}
