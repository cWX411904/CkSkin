package com.ck.ckskin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.ck.ckskin.skin.Skin;
import com.ck.ckskin.skin.SkinUtils;
import com.ck.skin_core.SkinManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Lance
 * @date 2018/3/12
 */

public class SkinActivity extends Activity {

    private static final String TAG = "wsj";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    List<Skin> skins = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        skins.add(new Skin("e0893ca73a972d82bcfc3a5a7a83666d",
                "1111111.skin", "app_skin-debug" +
                ".apk"));
    }

    public void change(View view) {
        //换肤
        Skin skin = skins.get(0);
        selectSkin(skin);
        SkinManager.getInstance().loadSkin(skin.path);
    }

    private void selectSkin(Skin skin) {
        File theme = new File(getFilesDir(), "theme");
        if (theme.exists() && theme.isFile()) {
            theme.delete();
        }
        theme.mkdirs();
        File skinFile = skin.getSkinFile(theme);
        if (skinFile.exists()) {
            if (DEBUG) Log.d(TAG, "SkinActivity selectSkin: " + "皮肤包已存在，开始换肤");
            return;
        }
        if (DEBUG) Log.d(TAG, "SkinActivity selectSkin: " + "皮肤包不存在，开始下载");
        FileOutputStream fos = null;
        InputStream is = null;
        //临时文件
        File tempSkin = new File(skinFile.getParentFile(), skin.name + ".temp");
        try {
            fos = new FileOutputStream(tempSkin);
            //假设下载皮肤包
            is = getAssets().open(skin.url);
            byte[] bytes = new byte[10240];
            int len;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
            //下载成功，将皮肤包信息insert已下载数据库
            if (DEBUG) Log.d(TAG, "SkinActivity selectSkin: " + "皮肤包下载完成开始校验");
            //皮肤包的md5校验，防止下载文件损坏（但是会减慢速度，从数据库查询已下载皮肤表数据库中保留的md5字段）
            if (TextUtils.equals(SkinUtils.getSkinMD5(tempSkin), skin.md5)) {
                if (DEBUG) Log.d(TAG, "SkinActivity selectSkin: " + "检验成功，修改文件名");
                tempSkin.renameTo(skinFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tempSkin.delete();
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void restore(View view) {
        SkinManager.getInstance().loadSkin(null);
    }
}





















