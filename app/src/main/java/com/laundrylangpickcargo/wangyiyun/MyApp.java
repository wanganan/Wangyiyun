package com.laundrylangpickcargo.wangyiyun;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.OnMessageItemClickListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;

/**
 * Created by Sinaan on 2016/11/23.
 */
public class MyApp extends Application{
    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Unicorn.init(this, "fea516cb68ae8d8d6dcc9bd63a02aa09", options(), new UILImageLoader());
    }

    // 如果返回值为null，则全部使用默认参数。
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        //开启省电特性
        SavePowerConfig savePowerConfig = new SavePowerConfig();
        savePowerConfig.activeDelay = 5000;//一次会话结束后，长连接可以在继续保持一段时间(0<10000)
        savePowerConfig.customPush = true;//如果有新消息，会自动转到后台配置的推送通道上。
        savePowerConfig.deviceIdentifier = "";//设置自定义推送的设备 ID。在七鱼推送的消息结构体中，会包含该字段。
        options.savePowerConfig = savePowerConfig;

        //设置ui
        UICustomization customization = new UICustomization();
//        customization.hideRightAvatar = true;
        customization.msgBackgroundUri = "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg";
        customization.rightAvatar = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
        options.uiCustomization = customization;

        //消息推送点击
        StatusBarNotificationConfig statusBarNotificationConfig = new StatusBarNotificationConfig();
        statusBarNotificationConfig.notificationEntrance = SecondActivity.class;
        options.statusBarNotificationConfig = statusBarNotificationConfig;

        //设置用户或者客服发送的文本消息中带有 URL 链接的点击事件（SDK默认会打开）
        options.onMessageItemClickListener = messageItemClickListener;
        return options;
    }

    public class UILImageLoader implements UnicornImageLoader {
        private static final String TAG = "UILImageLoader";

        @Override
        public Bitmap loadImageSync(String uri, int width, int height) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            // check cache
            boolean cached = MemoryCacheUtils.findCachedBitmapsForImageUri(uri, ImageLoader.getInstance().getMemoryCache()).size() > 0
                    || DiskCacheUtils.findInCache(uri, ImageLoader.getInstance().getDiskCache()) != null;
            if (cached) {
                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri, new ImageSize(width, height), options);
                if (bitmap == null) {
                    Log.e(TAG, "load cached image failed, uri =" + uri);
                }
                return bitmap;
            }

            return null;
        }

        @Override
        public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.loadImage(uri, new ImageSize(width, height), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    if (listener != null) {
                        listener.onLoadComplete(loadedImage);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    if (listener != null) {
                        listener.onLoadFailed(failReason.getCause());
                    }
                }
            });
        }
    }

    // 初始化代码
    OnMessageItemClickListener messageItemClickListener = new OnMessageItemClickListener() {

        // 响应 url 点击事件
        public void onURLClicked(Context context, String url) {
            // 打开内置浏览器等动作
            Log.e("","url=="+url);
        }
    };

}
