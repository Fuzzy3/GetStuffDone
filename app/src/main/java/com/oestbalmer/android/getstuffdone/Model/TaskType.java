package com.oestbalmer.android.getstuffdone.Model;


public class TaskType {
    private static final String PENDING_TYPE = "PENDING";
    private static final String TODAY_TYPE = "TODAY";
    private static final String COMPLETED_TYPE = "COMPLETED";

    public static String pending() {
        return PENDING_TYPE;
    }

    public static String today() {
        return TODAY_TYPE;
    }

    public static String completed() {
        return COMPLETED_TYPE;
    }
}
