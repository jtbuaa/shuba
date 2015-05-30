package com.qiwenge.android.mvp.presenter;

import android.content.Context;

import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.app.ReadApplication;
import com.qiwenge.android.mvp.model.LegalModel;
import com.qiwenge.android.mvp.ui.LegalUi;
import com.qiwenge.android.mvp.ui.LegalUiCallback;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/12.
 */
public class LegalPresenter extends Presenter<LegalUi, LegalUiCallback> {

    @Inject
    LegalModel mLegalModel;

    public LegalPresenter(Context context, LegalUi ui) {
        super(ui);
        ReadApplication.from(context).inject(this);
    }

    @Override
    protected void populateUi(LegalUi legalUi) {
        mLegalModel.getLegal(legalUi);
    }

    @Override
    protected LegalUiCallback createUiCallback(LegalUi legalUi) {
        return null;
    }
}
