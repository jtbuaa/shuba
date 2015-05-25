/*
 * Copyright 2015 qiwenge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiwenge.android.mvp.presenter;

import android.content.Context;

import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.app.ReadApplication;
import com.qiwenge.android.mvp.model.FeedbackModel;
import com.qiwenge.android.mvp.ui.FeedbackUi;
import com.qiwenge.android.mvp.ui.FeedbackUiCallback;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/25.
 */
public class FeedbackPresenter extends Presenter<FeedbackUi, FeedbackUiCallback> {

    @Inject
    FeedbackModel mFeedbackModel;

    Context mContext;

    public FeedbackPresenter(Context context) {
        ReadApplication.from(context).inject(this);
        mContext = context;
    }

    @Override
    protected void populateUi(FeedbackUi feedbackUi) {

    }

    @Override
    protected FeedbackUiCallback createUiCallback(final FeedbackUi feedbackUi) {
        return new FeedbackUiCallback() {
            @Override
            public void feedback(String content) {
                mFeedbackModel.feedback(mContext, content, feedbackUi);
            }
        };
    }
}
