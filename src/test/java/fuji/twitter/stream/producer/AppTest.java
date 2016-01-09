/**
 * AppTest.java
 *
 * @since 2015/11/01
 */
package fuji.twitter.stream.producer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Main Application Unit Test.
 * @author fuji-151a
 */
public class AppTest {

    @Before
    public void setUp() throws Exception {

    }

//    @Test
    public void test() throws Exception {
        // TODO Create Test Code.
//        String str = "Dec 24, 2015 1:36:06 AM";
        String str = "Dec 24, 2015 1:36:06";
        System.out.println(convertDate(str));
    }

    /**
     * 指定した日付フォーマットに変換.
     * @param date created_atの値
     * @return yyyyMMddHHmmの形式
     */
    private String convertDate(final String date) {
//        Date d = new Date(date);
//        String pattern = "MMM dd, yyyy HH:mm:ss a";
        String pattern = "MMM dd, yyyy HH:mm:ss";
        DateTime dt = DateTime.parse(date,
                DateTimeFormat.forPattern(pattern));
        System.out.println(dt.toString("MMM a"));
        return dt.toString();
//        return dt.toString("yyyyMMddHHmm");
    }

    @After
    public void tearDown() throws Exception {

    }
}