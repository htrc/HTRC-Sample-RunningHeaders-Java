package org.hathitrust.htrc.examples.textprocessing;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
    public static final String SCHEMA_VERSION;
    public static final DateTimeFormatter DATETIME_FORMAT;

    static {
        SCHEMA_VERSION = System.getProperty("SCHEMA_VERSION", "4.0");
        DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
    }
}
