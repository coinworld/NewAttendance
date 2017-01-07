package me.blog.tastedroid.attendance.ranking;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.GlobalOptions;
import me.blog.tastedroid.attendance.config.ConfigValues;
import me.blog.tastedroid.attendance.process.Processor;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Ranking {

    public void start() {
        ConfigValues conf = AttendServices.getFileManager().getConfig().getValues();
        if (!(conf.getRankingRefreshInterval() <= 0 || conf.getRankingPlayersPerPage() <= 0)) {
            if (GlobalOptions.DEBUG) {
                System.out.println("Ranking task scheduled.");
            }
            AttendServices.getProcessor().getTaskManager().scheduleAsync(
                    this::refresh, conf.getRankingRefreshInterval()
            );
        }
    }

    public class Info {

        private final String name;
        private final int count;

        private Info(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }
    }

    protected Paging<Info> ps;
    protected long lastUpdate = 0;

    public void refresh() {
        lastUpdate = System.currentTimeMillis();
        if (GlobalOptions.DEBUG) {
            System.out.println(new Date(lastUpdate) + ": Ranking is getting refreshed.");
        }
        Processor pro = AttendServices.getProcessor();
        List<Info> list = AttendServices.getFileManager().getUsers().getUserMap().entrySet().stream()
                .map(entry -> new Info(pro.getName(entry.getValue()), entry.getValue().getCount()))
                .sorted(Comparator.comparing(Info::getCount).reversed())
                .filter(info -> info.getCount() > 0 && info.getName() != null)
                .collect(Collectors.toList());

        ps = new Paging<>(
                list, Info.class, AttendServices.getFileManager().getConfig().getValues().getRankingPlayersPerPage()
        );
    }

    public Paging<Info> getPages() {
        return ps;
    }

    public long getLastUpdateTime() {
        return lastUpdate;
    }
}
