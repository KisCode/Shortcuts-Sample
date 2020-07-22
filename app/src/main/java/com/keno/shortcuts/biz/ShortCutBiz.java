package com.keno.shortcuts.biz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.keno.shortcuts.ContactActivity;
import com.keno.shortcuts.MainActivity;
import com.keno.shortcuts.R;
import com.keno.shortcuts.ScanActivity;
import com.keno.shortcuts.ShortcutSettingActivity;
import com.keno.shortcuts.WalletActivity;
import com.keno.shortcuts.WarningActivity;
import com.keno.shortcuts.constant.ShortcutConfig;
import com.keno.shortcuts.pojo.ShortcutModel;

import java.util.ArrayList;
import java.util.List;

public class ShortCutBiz {
    /***
     * 根据shortcutId生成 ShortcutModel
     * @param context context上下文
     * @param code shortcut ID
     * @param isAdd 是否新增
     * @return ShortcutModel
     */
    public static ShortcutModel getInstanceByCode(Context context, String code, boolean isAdd) {
        switch (code) {
            case ShortcutConfig.SHORTCUT_CONTACT:
                return new ShortcutModel(code, context.getString(R.string.title_contact_phone), R.mipmap.icon_contact_phone, isAdd);
            case ShortcutConfig.SHORTCUT_SCAN:
                return new ShortcutModel(code, context.getString(R.string.title_scan), R.mipmap.icon_scan, isAdd);
            case ShortcutConfig.SHORTCUT_WALLET:
                return new ShortcutModel(code, context.getString(R.string.title_wallet), R.mipmap.icon_wallet, isAdd);
            case ShortcutConfig.SHORTCUT_WARNING:
                return new ShortcutModel(code, context.getString(R.string.title_warning), R.mipmap.icon_warning, isAdd);
            default:
                return new ShortcutModel("", context.getString(R.string.app_name), R.drawable.ic_launcher_background, isAdd);
        }
    }


    public static List<ShortcutModel> convertToShortcutModelList(Context context, List<String> addList, boolean isAdd) {
        List<ShortcutModel> list = new ArrayList<>();
        for (String code : addList) {
            list.add(getInstanceByCode(context, code, isAdd));
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static List<ShortcutInfo> convertToShortcutInfoList(Context context, List<ShortcutModel> shortcutModelList) {
        List<ShortcutInfo> list = new ArrayList<>();
        for (ShortcutModel shortcutModel : shortcutModelList) {
            //构建动态快捷方式的详细信息
            ShortcutInfo shortcutInfoSetting = new ShortcutInfo.Builder(context, shortcutModel.getId())
                    .setShortLabel(shortcutModel.getTitle())
                    .setLongLabel(shortcutModel.getTitle())
                    .setIcon(Icon.createWithResource(context, shortcutModel.getIconRes()))
                    .setIntents(new Intent[]{getIntentById(context, shortcutModel.getId())
                    })
                    .build();
            list.add(shortcutInfoSetting);
        }
        return list;
    }

    private static Intent getIntentById(Context context, String code) {
        switch (code) {
            case ShortcutConfig.SHORTCUT_CONTACT:
                return new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, ContactActivity.class);
            case ShortcutConfig.SHORTCUT_SCAN:
                return new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, ScanActivity.class);
            case ShortcutConfig.SHORTCUT_WALLET:
                return new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, WalletActivity.class);
            case ShortcutConfig.SHORTCUT_WARNING:
                return new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, WarningActivity.class);
            default:
                return new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity.class);
        }
    }
}
