package com.qiwenge.android.mvp.ui;

import com.liuguangqiang.android.mvp.BaseUi;
import com.qiwenge.android.entity.MainMenuItem;

import java.util.List;

/**
 * Created by Eric on 15/5/9.
 */
public interface MainUi extends BaseUi<MainUiCallback> {

    void showMenu(List<MainMenuItem> list);

}
