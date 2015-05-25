package com.qiwenge.android.module;

import com.qiwenge.android.mvp.presenter.BookDetailPresenter;
import com.qiwenge.android.mvp.presenter.FeedbackPresenter;
import com.qiwenge.android.mvp.presenter.LegalPresenter;
import com.qiwenge.android.mvp.presenter.SearchPresenter;
import com.qiwenge.android.mvp.presenter.SettingsPresenter;

import dagger.Module;

/**
 * Created by Eric on 15/5/12.
 */
@Module(
        injects = {
                SettingsPresenter.class,
                BookDetailPresenter.class,
                LegalPresenter.class,
                SearchPresenter.class,
                FeedbackPresenter.class
        }
)
public class PresenterModule {
}
