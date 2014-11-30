package com.qiwenge.android.utils;

import android.test.AndroidTestCase;

import com.dev1024.utils.StringUtils;
import com.qiwenge.android.openudid.OpenUDIDUtils;

/**
 * Created by Eric on 14/11/30.
 */
public class OpenUDIDUtilsTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testGetOpenUDID() {
        String openUDID = OpenUDIDUtils.getOpenUDID(getContext());
        assertEquals(false, StringUtils.isEmptyOrNull(openUDID));
    }

}
