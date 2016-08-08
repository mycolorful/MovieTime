package per.yrj.movietime.utils;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jakewharton.disklrucache.DiskLruCache;

/**
 * 图片缓存帮助类
 * 包含内存缓存LruCache和磁盘缓存DiskLruCache
 * @author Javen
 */
public class ImageCacheUtil implements ImageCache {

    private String TAG=ImageCacheUtil.this.getClass().getSimpleName();

    //缓存类
    private static LruCache<String, Bitmap> mLruCache;

    private static DiskLruCache mDiskLruCache;
    //磁盘缓存大小
    private static final int DISK_MAX_SIZE = 10 * 1024 * 1024;

    public ImageCacheUtil(Context context, String cacheFileName) {
        // 获取应用可占内存的1/8作为缓存
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        // 实例化LruCache对象
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        try {
            // 获取DiskLruCache对象
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context.getApplicationContext(), cacheFileName)
                    , getAppVersion(context), 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从缓存（内存缓存，磁盘缓存）中获取Bitmap
     */
    @Override
    public Bitmap getBitmap(String url) {
        if (mLruCache.get(url) != null) {
            // 从LruCache缓存中取
            Debug.i("从内存获取");
            return mLruCache.get(url);
        } else {
            String key = MD5.toMD5(url);
            try {
                if (mDiskLruCache.get(key) != null) {
                    // 从DiskLruCache取
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    Bitmap bitmap = null;
                    if (snapshot != null) {
                        Debug.i("从磁盘获取");
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        // 存入LruCache缓存
                        mLruCache.put(url, bitmap);
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 存入缓存（内存缓存，磁盘缓存）
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // 存入LruCache缓存
        mLruCache.put(url, bitmap);
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5.toMD5(url);
        try {
            if (mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (bitmap.compress(CompressFormat.JPEG, 100, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
