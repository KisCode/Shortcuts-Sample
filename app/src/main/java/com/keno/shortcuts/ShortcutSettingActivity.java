package com.keno.shortcuts;

import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keno.shortcuts.adapter.ShortcutSettingAdapter;
import com.keno.shortcuts.biz.ShortCutBiz;
import com.keno.shortcuts.constant.ShortcutConfig;
import com.keno.shortcuts.pojo.ShortcutModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Description:  shortCut设置页面
 * Author: keno
 * Date : 2020/7/22 15:38
 **/
public class ShortcutSettingActivity extends AppCompatActivity {
    //可配置功能列表
    private final String[] SHORTCUT_CODE_LIST = new String[]{
            ShortcutConfig.SHORTCUT_CONTACT,
            ShortcutConfig.SHORTCUT_SCAN,
            ShortcutConfig.SHORTCUT_WALLET,
            ShortcutConfig.SHORTCUT_WARNING,
    };

    private TextView tvAdd;
    private RecyclerView recyclerAdd;
    private TextView tvUnadd;
    private RecyclerView recyclerUnadd;

    private ShortcutSettingAdapter addAdapter;
    private ShortcutSettingAdapter unAddAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_setting);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            initShortCutSetting();
        }

        setOnItemClick();
    }

    private void initView() {
        tvAdd = findViewById(R.id.tv_add);
        recyclerAdd = findViewById(R.id.recycler_add);
        tvUnadd = findViewById(R.id.tv_unadd);
        recyclerUnadd = findViewById(R.id.recycler_unadd);

        addAdapter = new ShortcutSettingAdapter(this, Collections.<ShortcutModel>emptyList());
        recyclerAdd.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdd.setAdapter(addAdapter);

        unAddAdapter = new ShortcutSettingAdapter(this, Collections.<ShortcutModel>emptyList());
        recyclerUnadd.setLayoutManager(new LinearLayoutManager(this));
        recyclerUnadd.setAdapter(unAddAdapter);

    }

    private void setOnItemClick() {
        addAdapter.setOnClickListener(new ShortcutSettingAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void onItemChildViewClick(View view, int pos) {
                ShortcutModel shortcutModel = addAdapter.getItem(pos);
                if (view.getId() == R.id.tv_add_shortcut_setting) {
                    shortcutModel.setAdd(false);
                    Log.i("getTitle", "remove:\t" + shortcutModel.getTitle());
                    addAdapter.remove(pos);
                    unAddAdapter.add(shortcutModel);
                    removeShortcutSetting(Arrays.asList(shortcutModel));
                }
            }
        });

        unAddAdapter.setOnClickListener(new ShortcutSettingAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N_MR1)
            @Override
            public void onItemChildViewClick(View view, int pos) {
                ShortcutModel shortcutModel = unAddAdapter.getItem(pos);
                shortcutModel.setAdd(true);
                if (view.getId() == R.id.tv_add_shortcut_setting) {
                    Log.i("getTitle", "Add:\t" + shortcutModel.getTitle());
                    unAddAdapter.remove(pos);
                    addAdapter.add(shortcutModel);
                    addShortcutSetting(Arrays.asList(shortcutModel));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void initShortCutSetting() {
        ShortcutManager shortcutManager = (ShortcutManager) getSystemService(SHORTCUT_SERVICE);

        if (shortcutManager == null) {
            return;
        }

        List<String> addCodeList = new ArrayList<>();
        List<String> unAddCodeList = new ArrayList<>();
        List<ShortcutInfo> shortcutInfoList = shortcutManager.getDynamicShortcuts();
        for (String code : SHORTCUT_CODE_LIST) {
            unAddCodeList.add(code);
            for (ShortcutInfo shortcutInfo : shortcutInfoList) {
                if (code.equals(shortcutInfo.getId())) {
                    addCodeList.add(code);
                    unAddCodeList.remove(code);
                    break;
                }
            }
        }
        addAdapter.setNewData(ShortCutBiz.convertToShortcutModelList(this, addCodeList, true));
        unAddAdapter.setNewData(ShortCutBiz.convertToShortcutModelList(this, unAddCodeList, false));
    }


    /***
     * 移除快捷方式配置
     * @param shortcutModelList
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void removeShortcutSetting(List<ShortcutModel> shortcutModelList) {
        ShortcutManager shortcutManager = (ShortcutManager) getSystemService(SHORTCUT_SERVICE);
        if (shortcutManager == null) {
            return;
        }

        List<String> shortcutIds = new ArrayList<>();
        for (ShortcutModel shortcutModel : shortcutModelList) {
            shortcutIds.add(shortcutModel.getId());
        }
        shortcutManager.removeDynamicShortcuts(shortcutIds);
    }

    /***
     * 添加快捷方式配置
     * @param shortcutModelList
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void addShortcutSetting(List<ShortcutModel> shortcutModelList) {
        ShortcutManager shortcutManager = (ShortcutManager) getSystemService(SHORTCUT_SERVICE);
        if (shortcutManager == null) {
            return;
        }
        shortcutManager.addDynamicShortcuts(ShortCutBiz.convertToShortcutInfoList(this, shortcutModelList));
    }
}
