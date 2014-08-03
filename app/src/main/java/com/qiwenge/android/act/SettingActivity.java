package com.qiwenge.android.act;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dev1024.utils.AppUtils;
import com.dev1024.utils.IntentUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.ThemeUtils;

/**
 * 设置。
 *
 * Created by John on 2014-7-9
 */
public class SettingActivity extends BaseActivity implements OnClickListener {

    private TextView tvVersionName;
    private TextView tvRating;
    private TextView tvLegal;

    private ImageView ivNightModel;
    private ImageView ivSaveModel;

    private RelativeLayout layoutNightModel;
    private RelativeLayout layoutSaveModel;
    private LinearLayout layoutContent;
    private ScrollView scrollContainer;

    private boolean selectSaveModel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.set_title);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_night_model:// 夜间模式
                setNightModel();
                break;
            case R.id.layout_save_model:// 控制流量
                setSaveModel();
                break;
            case R.id.set_tv_rating:// 评分
                IntentUtils.skipToMarket(getApplicationContext());
                break;
            case R.id.set_tv_legal://免责声明
                startActivity(LegalActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ImageLoaderUtils.isOpen()) {
            setCheckbox(ivSaveModel, true);
            selectSaveModel = true;
        }

        if(ThemeUtils.getIsNightModel()){
            setCheckbox(ivNightModel,true);
        }else{
            setCheckbox(ivNightModel,false);
        }

        ThemeUtils.setThemeBg(scrollContainer);
        setLineColor();
    }

    /**
     * 设置流量模式
     */
    private void setSaveModel() {
        if (selectSaveModel) {
            setCheckbox(ivSaveModel, false);
            ImageLoaderUtils.openLoader(getApplicationContext());
        } else {
            setCheckbox(ivSaveModel, true);
            ImageLoaderUtils.closeLoader(getApplicationContext());
        }
        selectSaveModel = !selectSaveModel;
    }

    /**
     * 设置夜间模式
     */
    private void setNightModel() {
        if (ThemeUtils.getIsNightModel()) {
            setCheckbox(ivNightModel, false);
            ThemeUtils.setNightModle(getApplicationContext(),false);
        }
        else {
            setCheckbox(ivNightModel, true);
            ThemeUtils.setNightModle(getApplicationContext(),true);
        }
        ThemeUtils.setThemeBg(scrollContainer);
        setLineColor();
    }

    private void initViews() {
        layoutContent=(LinearLayout)this.findViewById(R.id.layout_content);
        scrollContainer=(ScrollView)this.findViewById(R.id.scrollView_container);

        tvLegal=(TextView)this.findViewById(R.id.set_tv_legal);
        tvLegal.setOnClickListener(this);

        tvRating = (TextView) this.findViewById(R.id.set_tv_rating);
        tvRating.setOnClickListener(this);

        tvVersionName = (TextView) this.findViewById(R.id.tv_version_name);
        tvVersionName.setText(AppUtils.getVersionName(getApplicationContext()));

        ivNightModel = (ImageView) this.findViewById(R.id.iv_night_model);
        layoutNightModel = (RelativeLayout) this.findViewById(R.id.layout_night_model);
        layoutNightModel.setOnClickListener(this);

        ivSaveModel = (ImageView) this.findViewById(R.id.iv_save_model);
        layoutSaveModel = (RelativeLayout) this.findViewById(R.id.layout_save_model);
        layoutSaveModel.setOnClickListener(this);
    }

    private void setLineColor(){
        int count=layoutContent.getChildCount();
        View view;
        for(int i=0;i<count;i++){
            view=layoutContent.getChildAt(i);
            if(view instanceof ImageView){
                ThemeUtils.setThemeLine(view);
            }
        }
    }

    private void setCheckbox(ImageView iv, boolean ischecked) {
        if (ischecked) {
            iv.setBackgroundResource(R.drawable.ic_checkbox_selected);
        } else {
            iv.setBackgroundResource(R.drawable.ic_checkbox_normal);
        }
    }

}
