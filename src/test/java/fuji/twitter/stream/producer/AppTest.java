/**
 * AppTest.java
 *
 * @since 2015/11/01
 */
package fuji.twitter.stream.producer;

import org.joda.time.DateTime;
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

    @Test
    public void test() throws Exception {
        // TODO Create Test Code.
        String str = "Dec 24, 2015 1:36:06 AM";
        System.out.println(convertDate(str));
    }

    /**
     * 指定した日付フォーマットに変換.
     * @param date created_atの値
     * @return yyyyMMddHHmmの形式
     */
    private String convertDate(final String date) {
        Calendar cal = Calendar.getInstance();
        Date d = new Date(date);
        DateTime dt = new DateTime(date);
        return dt.toString("yyyyMMddHHmm");
    }

    @After
    public void tearDown() throws Exception {

    }
}