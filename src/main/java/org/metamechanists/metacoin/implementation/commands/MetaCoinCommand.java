package org.metamechanists.metacoin.implementation.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metacoin.core.ItemStacks;
import org.metamechanists.metacoin.core.Leaderboard;
import org.metamechanists.metacoin.implementation.runnables.WarrantyVoidRunnable;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinItem;
import org.metamechanists.metacoin.implementation.slimefun.MetaCoinMiner;
import org.metamechanists.metacoin.implementation.slimefun.Upgrades;
import org.metamechanists.metacoin.utils.Keys;
import org.metamechanists.metalib.utils.ItemUtils;


@SuppressWarnings("unused")
@CommandAlias("metacoin|blockchain")
public class MetaCoinCommand extends BaseCommand {

    @Subcommand("deposit")
    @Description("Deposits your pathetically small number of coins to the leaderboard")
    @CommandPermission("metacoin.command.deposit")
    public static void deposit(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        final long value = MetaCoinItem.getTotalCoinValue(player);
        MetaCoinItem.removeCoins(player, value);
        Leaderboard.updateLeaderboard(player.getUniqueId(), Leaderboard.getValue(player.getUniqueId()) + value);
    }

    @Subcommand("resetleaderboard")
    @Description("Resets the leaderboard, OBLITERATING all the hard work of your players")
    @CommandPermission("metacoin.command.resetleaderboard")
    public static void resetLeaderboard(CommandSender sender) {
        Leaderboard.reset();
    }

    @Subcommand("compress")
    @Description("Compresses all coins in your inventory using ADVANCED NANOTECHNOLOGY")
    @CommandPermission("metacoin.command.compress")
    public static void compress(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        final long value = MetaCoinItem.getTotalCoinValue(player);
        MetaCoinItem.removeCoins(player, value);
        ItemUtils.addOrDropItem(player, MetaCoinItem.withTotalValue(value));
    }

    @Subcommand("invest")
    @Description("Start your adventure in blockchain by acquiring a MetaMiner")
    @CommandPermission("metacoin.command.invest")
    public static void invest(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        if (PersistentDataAPI.getOptionalBoolean(player, Keys.receivedMiner).orElse(false)) {
            player.sendMessage("You ABSOLUTE BUFFON. you cannot acquire a second MetaMiner. You had just ONCE CHANCE to redeem yourself in this life, and you failed.");
            return;
        }

        PersistentDataAPI.setBoolean(player, Keys.receivedMiner, true);
        ItemUtils.addOrDropItemMainHand(player, ItemStacks.metaCoinMiner(player));
    }

    @Subcommand("reset")
    @Description("Allows a player to run /invest again to get another miner")
    @CommandPermission("metacoin.command.reset")
    public static void resetStuff(CommandSender sender, @NotNull Player player) {
        PersistentDataAPI.remove(player, Keys.minerPlaced);
        PersistentDataAPI.remove(player, Keys.receivedMiner);
    }

    @Subcommand("becomeslag")
    @Description("Uses the power of CRYPTOGRAPHY to turn the metaminer you're looking at into slag")
    @CommandPermission("metacoin.command.becomeslag")
    public static void becomeSlag(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        final Block block = player.getTargetBlockExact(7);
        if (block != null && BlockStorage.check(block) instanceof MetaCoinMiner) {
            ItemUtils.addOrDropItemMainHand(player, ItemStacks.machineSlag(player, Upgrades.getLevels(block.getLocation())));
        }
    }

    @Subcommand("voidwarranty")
    @Description("Uses the power of CRYPTOGRAPHY to void the warranty on the metaminer you're looking at")
    @CommandPermission("metacoin.command.voidwarranty")
    public static void voidWarranty(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }

        final Block block = player.getTargetBlockExact(7);
        if (block != null && BlockStorage.check(block) instanceof MetaCoinMiner miner) {
            new WarrantyVoidRunnable(player, block, miner.getDisplayGroup(block));
        }
    }
}
