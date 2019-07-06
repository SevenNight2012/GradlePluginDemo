package com.xxc.dev.gradle.plugin;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        Pattern androidSupport = Pattern.compile("android/support.*");
        String path = "android/support/v7";
        System.out.println(androidSupport.matcher(path).matches());
        Object a = getValue();
        int ten = 10;
        Integer i = new Integer(String.valueOf(a));

        int dex = ten - i;
        System.out.println(dex + "  ---");
    }

    public Object getValue() {
        int a = 1;
        return a;
    }
}