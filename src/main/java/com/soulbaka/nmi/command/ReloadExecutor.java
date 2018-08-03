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

import com.soulbaka.nmi.NucleusMultiInv;
import com.soulbaka.nmi.util.Utils;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;


public class ReloadExecutor extends CommandExecutorBase {

    @Nonnull
    @Override
    public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
        Game game = NucleusMultiInv.getNucleusMultiInv().getGame();

        if (src.hasPermission("nucleusmultiinv.reload.execute")) {
            Utils.doReload();
            src.sendMessage(Text.builder("reloaded").build());
        } else {
            src.sendMessage(Text.builder("no permission!").build());
        }
        return CommandResult.success();
    }

    @Nonnull
    @Override
    public String[] getAliases() {
        return new String[] { "nmireload" };
    }

    @Nonnull
    @Override
    public CommandSpec getSpec() {
        return CommandSpec.builder().description(Text.of("Reload Command"))
                .permission("nucleusmultiinv.reload")
                .arguments(GenericArguments.none())
                .executor(this).build();
    }
}