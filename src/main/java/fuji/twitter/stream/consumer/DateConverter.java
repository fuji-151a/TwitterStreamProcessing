/**
 * DateConverter.java
 *
 * @since 2015/12/31
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.consumer;

import org.joda.time.DateTime;

import java.util.Date;

/**
 *
 * @author fuji-151a
 */
public class DateConverter {

    private Date date;
    private DateTime dt;

    public DateConverter() {
        this.date = new Date();
        this.dt = new DateTime(date);
    }

    public DateConverter(final String d) {
        this.date = new Date(d);
        this.dt = new DateTime(date);
    }

    /**
     * 指定した日付フォーマットに変換.
     * @return yyyyMMddHHmmの形式
     */
    public String convertDate() {
        return dt.toString("yyyyMMddHHmm");
    }
}
