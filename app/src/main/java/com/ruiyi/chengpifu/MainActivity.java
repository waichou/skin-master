package com.ruiyi.chengpifu;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ruiyi.skin_core.SkinManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  实现流程：1.为activity在执行setContentView前设置实例化view的监听器，处理在application中onActivityCreated中
 *           2.通过监听器，来统计获取要替换的资源以及对应的view实例（SkinFactory控制统计流程，具体view & attrs统计到SkinAttribute中）
 *           3.根据统计的相关参数SkinAttribute，对应获取外部apk中对应的资源（SkinResourcess中处理）进行替换操作
 *           额外：SkinPreference 这种类用于控制加载皮肤时机，首次打开不会换肤，换过之后下次自动执行换肤流程
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void apply(View view) {
        SkinManager.getInstance().loadSkin("/sdcard/skin.apk");
    }

    public void restore(View view) {
        SkinManager.getInstance().loadSkin("");
    }

    /**
     * 从assets目录下拷贝文件
     *
     * @param context            上下文
     * @param assetsFilePath     文件的路径名如：SBClock/0001cuteowl/cuteowl_dot.png
     * @param targetFileFullPath 目标文件路径如：/sdcard/SBClock/0001cuteowl/cuteowl_dot.png
     */
    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d("Tag", "copyFileFromAssets ");
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d("Tag", "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copyFile(InputStream in, String targetPath) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(targetPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            in.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(View view) {
        copyFileFromAssets(this, "skin.apk", "/sdcard/skin.apk");
    }
}
