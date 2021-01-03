package au.edu.jcu.cp3406.smartereveryday;

import org.junit.Test;

import java.util.Date;

import au.edu.jcu.cp3406.smartereveryday.utils.DateHelper;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    DateHelper dateHelper = new DateHelper();

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testDateHelper(){
        Date last = new Date();
        last.setTime(1586005200000L); // date in mills 04-04-2020
        boolean diff = dateHelper.isNextDay(last);
        System.out.println(diff);
        assertFalse(diff);
    }
}