
**在Android 7.1后新增 shortcut实现桌面快捷方式，可实现类似iOS 3D-Touch的效果，给应用配置快捷方式后，长按桌面图标可快速进入对应目标页面。**

该项目主要实现了类似支付宝动态配置桌面快捷。


![setting](https://github.com/KisCode/Shortcuts-Sample/blob/master/image/Screenshot_1.png)

![shortcuts](https://github.com/KisCode/Shortcuts-Sample/blob/master/image/Screenshot_2.png)

Shortcut配置快捷方式 支持静态配置 和 动态配置两种方式

## 静态配置Shortcut快捷方式
#### 1.  在res/xml目录下声明 shortcuts快捷方式,包含多个shortcut Item,文件名为shortcuts_home.xml
 - icon ：快捷方式图标
 - shortcutId： 快捷方式的Id 唯一标志
 - shortcutLongLabel 和shortcutShortLabel：快捷方式名称
 - intent: 点击该快捷方式的意图，可配置后续打开指定
```
<?xml version="1.0" encoding="utf-8"?>
<shortcuts xmlns:android="http://schemas.android.com/apk/res/android">
    <shortcut
        android:enabled="true"
        android:icon="@mipmap/icon_scan"
        android:shortcutDisabledMessage="@string/shortcutDisabledMessage"
        android:shortcutId="scan"
        android:shortcutLongLabel="@string/title_scan"
        android:shortcutShortLabel="@string/title_scan">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.keno.shortcuts.ScanActivity"
            android:targetPackage="com.keno.shortcuts" />
        <categories android:name="android.shortcut.conversation" />
    </shortcut>

    <shortcut
        android:enabled="true"
        android:icon="@mipmap/icon_wallet"
        android:shortcutDisabledMessage="@string/shortcutDisabledMessage"
        android:shortcutId="wallet"
        android:shortcutLongLabel="@string/title_wallet"
        android:shortcutShortLabel="@string/title_wallet">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.keno.shortcuts.WalletActivity"
            android:targetPackage="com.keno.shortcuts" />
        <categories android:name="android.shortcut.conversation" />
    </shortcut>
    <shortcut
        android:enabled="true"
        android:icon="@mipmap/icon_contact_phone"
        android:shortcutDisabledMessage="@string/shortcutDisabledMessage"
        android:shortcutId="contactPhone"
        android:shortcutLongLabel="@string/title_contact_phone"
        android:shortcutShortLabel="@string/title_contact_phone">
        <intent
            android:action="android.intent.action.VIEW"
            android:targetClass="com.keno.shortcuts.ContactActivity"
            android:targetPackage="com.keno.shortcuts" />
        <categories android:name="android.shortcut.conversation" />
    </shortcut>
</shortcuts>
```


#### 2.  在AndroidManifest.xml的 启动Activity节点下 指定第一步声明的shortcuts；

```xml
<meta-data
    android:name="android.app.shortcuts"
    android:resource="@xml/shortcuts_home" />
```

完整代码如下：

```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>

    <!--静态声明shortcut-->
    <meta-data
        android:name="android.app.shortcuts"
        android:resource="@xml/shortcuts_home" />

</activity>
```

#### 完成以上两步后，运行程序，长按桌面图标，就能弹出快捷方式了

---

## 动态配置Shortcut快捷方式，支持新增、删除快捷配置
#### 1. 初始化获取，设置默认配置方式

注意：ShortcutManager api为Android 7.1以后新增，只能运行在Build.VERSION_CODES.N_MR1以上版本；

动态配置需构建一个ShortcutInfo 同样需要指定静态配置以下参数
 - icon ：快捷方式图标
 - shortcutId： 快捷方式的Id 唯一标志
 - shortcutLongLabel 和shortcutShortLabel：快捷方式名称
 - intent: 点击该快捷方式的意图，可配置后续打开指定

```java
//设置指定动态方式
shortcutManager.setDynamicShortcuts(shortcutInfoList);
```


```java
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

            shortcutManager.setDynamicShortcuts(shortcutInfoList);
        }

    }
```

#### 2. 新增配置项
将新增构建ShortcutInfo，并添加
源码如下：
```
/**
 * Publish the list of dynamic shortcuts.  If there are already dynamic or pinned shortcuts with
 * the same IDs, each mutable shortcut is updated.
 *
 * <p>This API will be rate-limited.
 *
 * @return {@code true} if the call has succeeded. {@code false} if the call is rate-limited.
 *
 * @throws IllegalArgumentException if {@link #getMaxShortcutCountPerActivity()} is exceeded,
 * or when trying to update immutable shortcuts.
 *
 * @throws IllegalStateException when the user is locked.
 */
public boolean addDynamicShortcuts(@NonNull List<ShortcutInfo> shortcutInfoList) {
    try {
        return mService.addDynamicShortcuts(mContext.getPackageName(),
                new ParceledListSlice(shortcutInfoList), injectMyUserId());
    } catch (RemoteException e) {
        throw e.rethrowFromSystemServer();
    }
}
```


```
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
```

#### 3. 删除配置项
- 根据ShortcutInfo的唯一标志Id即可删除
-
```
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
```


