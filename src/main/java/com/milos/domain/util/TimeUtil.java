package com.milos.domain.util;

import java.util.Date;

public class TimeUtil {

    public static long diffInMillis(Date dt1, Date dt2) {
        return Math.abs(dt1.getTime() - dt2.getTime());
    }
}
