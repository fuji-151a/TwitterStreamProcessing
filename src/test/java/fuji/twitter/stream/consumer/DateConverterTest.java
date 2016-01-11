package fuji.twitter.stream.consumer;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * DateConverterTest.java
 *
 * @since 2015/12/31
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
public class DateConverterTest {

    @Test
    public void convertDateTest() {
        String date = "Dec 31, 2015 1:00:11 AM";
        DateConverter dc = new DateConverter(date);
        String expected = "201512310100";
        assertThat(dc.convertDate(), is(expected));
    }

    @Test
    public void test() {
        Date date = new Date();
        DateTime dateTime = new DateTime(date);
        System.out.println(dateTime.getMillis());
    }

}