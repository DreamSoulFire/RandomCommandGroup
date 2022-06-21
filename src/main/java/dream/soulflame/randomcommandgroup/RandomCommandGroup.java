package dream.soulflame.randomcommandgroup;

import dream.soulflame.flamecore.utils.SendUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static dream.soulflame.flamecore.utils.SendUtil.*;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static dream.soulflame.randomcommandgroup.CommandGroup.*;

public final class RandomCommandGroup extends JavaPlugin implements TabExecutor {


    public static String prefixMsg = "&7| &aRandomCommandGroup &7|";
    private static RandomCommandGroup plugin;

    public static RandomCommandGroup getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        String[] startMsg = {"&d插件§b加载§a成功", "&d插件版本§f: &a" + getDescription().getVersion() + " &e加载配置..."};
        for (String start : startMsg) SendUtil.message(prefixMsg + start);

        plugin = this;
        saveDefaultConfig();

        String[] finishMsg = {"&b加载完成", "&b插件开始运行"};
        for (String finish : finishMsg) SendUtil.message(prefixMsg + finish);

    }

    @Override
    public void onDisable() {
        String[] closeMsg = {"&d插件&c卸载&a成功", "&d插件版本&f: &a" + getDescription().getVersion(), "&e感谢使用本插件"};
        for (String close : closeMsg) SendUtil.message(prefixMsg + close);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<String> noPermission = getConfig().getStringList("NoPermission");
        List<String> reloadMsg = getConfig().getStringList("Reload");
        List<String> cantConsoleMsg = getConfig().getStringList("CantConsole");

        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("randomcommandgroup.help"))
                for (String helpMsg : getConfig().getStringList("Help"))
                    sender.sendMessage(reColor(reName(sender, helpMsg)));
            else for (String noPer : noPermission) actions(sender, noPer);
            return true;
        }

        if (args.length == 1 && "run".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                if (sender.hasPermission("randomcommandgroup.run")) {
                    Player player = ((Player) sender).getPlayer();
                    for (String noEnough : getConfig().getStringList("ArgsNoEnough")) actions(player, noEnough);
                } else for (String noPer : noPermission) actions(sender, noPer);
                return true;
            } else for (String cant : cantConsoleMsg) actions(sender, cant);
            return true;
        }

        if (args.length == 2 && "run".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                if (sender.hasPermission("randomcommandgroup.run")) {
                    Player player = ((Player) sender).getPlayer();
                    build(args[1], player);
                } else for (String noPer : noPermission) actions(sender, noPer);
            } else for (String cant : cantConsoleMsg) actions(sender, cant);
            return true;
        }

        if (args.length > 2 &&  "run".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                if (sender.hasPermission("randomcommandgroup.run")) {
                    Player player = ((Player) sender).getPlayer();
                    boolean online = false;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        String name = onlinePlayer.getName();
                        if (!args[1].equalsIgnoreCase(name)) continue;
                        Player target = Bukkit.getPlayer(args[1]);
                        build(args[1], target);
                        online = true;
                    }
                    if (!online) for (String playerOffline : getPlugin().getConfig().getStringList("PlayerOffline"))
                        actions(player, playerOffline);
                } else for (String noPer : noPermission) actions(sender, noPer);
            } else for (String cant : cantConsoleMsg) actions(sender, cant);
            return true;
        }

        if ("reload".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("randomcommandgroup.reload")) {
                    for (String reload : reloadMsg) actions(player, reload);
                    reloadConfig();
                } else for (String noPer : noPermission) actions(sender, noPer);
            } else {
                for (String reload : reloadMsg) actions(sender, reload);
                reloadConfig();
            }
            return true;
        }
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return TabList.returnList(args, args.length);
    }
}


