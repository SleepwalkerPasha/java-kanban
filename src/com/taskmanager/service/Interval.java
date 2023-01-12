package com.taskmanager.service;

import java.time.LocalDateTime;
import java.util.Objects;

public class Interval {
    private LocalDateTime mFrom;

    private LocalDateTime mTo;

    public Interval(LocalDateTime mFrom, LocalDateTime mTo) {
        this.mFrom = mFrom;
        this.mTo = mTo;
    }

    public LocalDateTime getmFrom() {
        return mFrom;
    }

    public void setmFrom(LocalDateTime mFrom) {
        this.mFrom = mFrom;
    }

    public LocalDateTime getmTo() {
        return mTo;
    }

    public void setmTo(LocalDateTime mTo) {
        this.mTo = mTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;
        Interval interval = (Interval) o;
        return mFrom.equals(interval.mFrom) && mTo.equals(interval.mTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mFrom, mTo);
    }
}
