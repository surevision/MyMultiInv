
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
import java.util.Optional;


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