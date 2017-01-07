package me.blog.tastedroid.attendance.bukkit;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.Status;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.Tools;
import me.blog.tastedroid.attendance.process.bukkit.BukkitProcessor;
import me.blog.tastedroid.attendance.ranking.Paging;
import me.blog.tastedroid.attendance.ranking.Ranking;
import me.blog.tastedroid.attendance.ranking.Ranking.Info;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public final class BukkitCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("attend")) {
            OfflinePlayer p;
            if (args.length == 0) {
                if (cs instanceof Player) {
                    p = (Player) cs;
                } else {
                    cs.sendMessage(ChatColor.RED + "플레이어여야 합니다.");
                    return true;
                }
            } else {
                p = Bukkit.getOfflinePlayer(args[0]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "플레이어가 존재하지 않습니다.");
                    return true;
                }
            }
            UserInfo user = AttendServices.getFileManager().getUsers().getUser(p.getUniqueId());
            Status status = Tools.checkStatus(user);

            cs.sendMessage(ChatColor.YELLOW + p.getName() + " 출석: " + ChatColor.GOLD + user.getCount() + "일");
            cs.sendMessage(BukkitProcessor.getFormattedString(status.getMessage()));

            if (status.equals(Status.SUCCESS) && p.getName().equals(cs.getName())) {
                user.setLastCheck(System.currentTimeMillis());
                AttendServices.getProcessor().launchCommand(
                        AttendServices.getFileManager()
                                .getConfig().getValues().getCommands(user.getCount()),
                        p.getName()
                );
            }
        } else if (cmd.getName().equalsIgnoreCase("attendrank")) {
            Ranking ranking = AttendServices.getRanking();
            if (args.length >= 1 && Arrays.stream(args).anyMatch(str -> str.equals("-r"))) {
                ranking.refresh();
                cs.sendMessage(ChatColor.YELLOW + "랭킹을 새로고침했습니다.");
                return true;
            }

            Paging<Info> ps = ranking.getPages();
            boolean showDays = AttendServices.getFileManager().getConfig().getValues().showDays();

            if (ps.getPages() == 0) {
                cs.sendMessage("랭킹이 없습니다.");
                return true;
            }

            int page = 0;
            if (args.length > 0) {
                try {
                    int input = Integer.parseInt(args[0]) - 1;
                    if (input > 0) {
                        page = input;
                    }
                } catch (Exception e) {

                }
            }

            if (page >= ps.getPages()) {
                page = ps.getPages() - 1;
            }

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            cal.setTimeInMillis(ranking.getLastUpdateTime());
            cs.sendMessage(ChatColor.GOLD + String.format("---> 출석 랭킹 - %d시 %d분 (%d/%d)",
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), page + 1, ps.getPages()));

            for (Info info : ps.getPage(page)) {
                if (info != null) {
                    cs.sendMessage(
                            ChatColor.YELLOW + " * " + info.getName() + (showDays ? " (" + info.getCount() + "일)" : "")
                    );
                }
            }
        }
        return true;
    }

}
