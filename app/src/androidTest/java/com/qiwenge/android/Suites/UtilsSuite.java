package com.qiwenge.android.suites;

import android.test.AndroidTestCase;

import com.qiwenge.android.utils.ApiUtilsTest;
import com.qiwenge.android.utils.OpenUDIDUtilsTest;

import junit.framework.TestSuite;

/**
 * Created by Eric on 14/11/30.
 */
public class UtilsSuite extends AndroidTestCase {


    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTestSuite(ApiUtilsTest.class);
        testSuite.addTestSuite(OpenUDIDUtilsTest.class);
        return testSuite;
    }

}
