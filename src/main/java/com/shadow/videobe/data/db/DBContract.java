package com.shadow.videobe.data.db;

import android.provider.BaseColumns;

public class DBContract {

    static abstract class ToolTable implements BaseColumns {
        static final String TABLE_NAME = "tools";
        static final String COL_NAME = "name";
        static final String COL_PIC = "pic";
        static final String COL_DETAILS = "details";
    }
}
