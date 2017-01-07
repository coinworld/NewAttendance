package me.blog.tastedroid.attendance.sponge;

import com.google.common.collect.ImmutableMap;
import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.Status;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.Tools;
import me.blog.tastedroid.attendance.process.sponge.SpongeProcessor;
import me.blog.tastedroid.attendance.ranking.Paging;
import me.blog.tastedroid.attendance.ranking.Ranking;
import me.blog.tastedroid.attendance.ranking.Ranking.Info;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;

import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;

public final class SpongeCommands {

    private static class AttendCmd implements CommandExecutor {

        private final TextTemplate SHOW_DAYS = of(arg("player").color(TextColors.YELLOW), TextColors.YELLOW, " 출석 수: ",
                arg("count").color(TextColors.GOLD), TextColors.GOLD, "일");
        private final Text PLAYER_ONLY = Text.builder("플레이어만이 가능합니다.").color(TextColors.GOLD).build();

        @Override
        public CommandResult execute(CommandSource cs, CommandContext cc) throws CommandException {
            Optional<User> userOptional = cc.getOne("target");
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserInfo info = AttendServices.getFileManager().getUsers().getUser(user.getUniqueId());
                Status status = Tools.checkStatus(info);
                cs.sendMessage(SHOW_DAYS,
                        ImmutableMap.of("player", Text.of(user.getName()), "count", Text.of(info.getCount() + ""))
                );
                cs.sendMessage(SpongeProcessor.getFormattedText(status.getMessage()));
                if (status.equals(Status.SUCCESS) && cs.getName().equals(user.getName()) /* 바로 equals 써도 되나 */) {
                    info.setLastCheck(System.currentTimeMillis());
                    AttendServices.getProcessor().launchCommand(
                            AttendServices.getFileManager()
                                    .getConfig().getValues().getCommands(info.getCount()),
                            user.getName()
                    );
                }
                return CommandResult.success();
            } else {
                throw new CommandException(PLAYER_ONLY);
            }
        }

    }

    private static class RankingCmd implements CommandExecutor {

        private final TextTemplate DESC = of(TextColors.GOLD,
                "---> 출석 랭킹 - ", arg("hour"), "시 ", arg("min"), "분 (", arg("current"), "/", arg("max"), ")");

        @Override
        public CommandResult execute(CommandSource cs, CommandContext cc) throws CommandException {
            Ranking ranking = AttendServices.getRanking();
            if (cc.hasAny("r")) {
                ranking.refresh();
                cs.sendMessage(Text.builder("랭킹을 새로고침했습니다.").color(TextColors.YELLOW).build());
                return CommandResult.success();
            }

            Paging<Info> ps = ranking.getPages();
            boolean showDays = AttendServices.getFileManager().getConfig().getValues().showDays();

            if (ps.getPages() == 0) {
                cs.sendMessage(Text.of("랭킹이 없습니다."));
                return CommandResult.empty();
            }

            int page = cc.<Integer>getOne("page").orElse(1) - 1;
            if (page < 0) {
                page = 0;
            }

            if (page >= ps.getPages()) {
                page = ps.getPages() - 1;
            }

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
            cal.setTimeInMillis(ranking.getLastUpdateTime());

            cs.sendMessage(DESC,
                    ImmutableMap.of(
                            "hour", Text.of(cal.get(Calendar.HOUR_OF_DAY)),
                            "min", Text.of(cal.get(Calendar.MINUTE)),
                            "current", Text.of(page + 1),
                            "max", Text.of(ps.getPages())));

            TextTemplate info = !showDays ? of(TextColors.YELLOW, " * ", arg("name"))
                    : of(TextColors.YELLOW, " * ", arg("name"), " (", arg("count"), "일)");

            for (Info i : ps.getPage(page)) {
                if (i != null) {
                    cs.sendMessage(info, ImmutableMap.of("name", Text.of(i.getName()), "count", Text.of(i.getCount())));
                }
            }

            return CommandResult.empty();
        }

    }

    public static CommandExecutor newAttendCommand() {
        return new AttendCmd();
    }

    public static CommandExecutor newRankingCommand() {
        return new RankingCmd();
    }

     /*
     왜 new 로 했냐면 private static 선언은 lazy 초기화가 안 되기 때문에 이를 버킷에서 실행 시
     오류가 발생할 것 같았기 때문, 뭐 해봐야 아는 일이지만.
      */
}
