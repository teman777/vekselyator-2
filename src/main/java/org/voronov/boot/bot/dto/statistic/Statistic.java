package org.voronov.boot.bot.dto.statistic;

import org.joda.time.DateTime;

public class Statistic {

    private DateTime from;
    private DateTime to;

    public DateTime getFrom() {
        return from;
    }

    public void setFrom(DateTime from) {
        this.from = from;
    }

    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
    }
}
