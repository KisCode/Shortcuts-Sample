package com.keno.shortcuts;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.keno.shortcuts.constant.ShortcutConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            initShortCutsConfig();
            initShortcuts();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    void initShortCutsConfig() {
        ShortcutManager scManager = getSystemService(ShortcutManager.class);
        List<ShortcutInfo> list = scManager.getDynamicShortcuts();
        for (ShortcutInfo shortcutInfo : list) {
            Log.i("ShortcutManager", shortcutInfo.getShortLabel().toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void initShortcuts() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        if (shortcutManager == null) {
            return;
        }

        List<ShortcutInfo> shortcutInfoList = shortcutManager.getDynamicShortcuts();
        if (shortcutInfoList.isEmpty()) {
            shortcutInfoList = new ArrayList<>();

            //构建动态快捷方式的详细信息
            ShortcutInfo shortcutInfoSetting = new ShortcutInfo.Builder(this, ShortcutConfig.SHORTCUT_SETTING)
                    .setShortLabel(getString(R.string.title_shortcut_setting))
                    .setLongLabel(getString(R.string.title_shortcut_setting))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_settings_black))
                    .setIntents(new Intent[]{
                            new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, ShortcutSettingActivity.class)
                    })
                    .build();
            shortcutInfoList.add(shortcutInfoSetting);

            //设置指定动态方式
            shortcutManager.setDynamicShortcuts(shortcutInfoList);
        }

    }

}
