package com.qiwenge.android.utils;

import android.test.InstrumentationTestCase;

/**
 * Created by Eric on 14/11/28.
 */
public class ApiUtilsTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBuild() {
        String result = "http://api.qiwenge.com/books";
        assertEquals(result, ApiUtils.build("books"));
    }

    public void testBuildWith2Params() {
        String result = "http://api.qiwenge.com/books/id";
        assertEquals(result, ApiUtils.build("books", "id"));
    }

    public void testBuildWith3Params() {
        String result = "http://api.qiwenge.com/books/id/test";
        assertEquals(result, ApiUtils.build("books", "id", "test"));
    }
}