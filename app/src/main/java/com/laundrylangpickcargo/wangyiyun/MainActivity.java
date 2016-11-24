package com.laundrylangpickcargo.wangyiyun;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.qiyukf.unicorn.api.YSFUserInfo;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView wangyiyu = (TextView) findViewById(R.id.wangyiyu);

        Unicorn.addUnreadCountChangeListener(mUnreadCountListener, true);
        wangyiyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开客服窗口时调用该函数，设置访客资料。
                YSFUserInfo userInfo = new YSFUserInfo();
                userInfo.userId = "uid";
                userInfo.data = "[{\"key\":\"real_name\", \"value\":\"土豪\"}," +
                        "{\"key\":\"mobile_phone\", \"value\":\"13522267471\",\"hidden\":false}," +
//                        "{\"key\":\"email\", \"value\":\"13800000000@163.com\"}" +
//                        "{\"index\":0, \"key\":\"account\", \"label\":\"账号\", \"value\":\"zhangsan\" , \"href\":\"http://example.domain/user/zhangsan\"}" +
//                        "{\"index\":1, \"key\":\"sex\", \"label\":\"性别\", \"value\":\"先生\"}" +
//                        "{\"index\":5, \"key\":\"reg_date\", \"label\":\"注册日期\", \"value\":\"2015-11-16\"}" +
//                        "{\"index\":6, \"key\":\"last_login\", \"label\":\"上次登录时间\", \"value\":\"2015-12-22 15:38:54\"}" +
                        "]";
                Unicorn.setUserInfo(userInfo);

                // 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入三个参数分别为来源页面的url，来源页面标题，来源页面额外信息（可自由定义）
                // 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                ConsultSource source = new ConsultSource("", "123", "custom information string");

                //发送商品信息给客服
                ProductDetail.Builder builder = new ProductDetail.Builder();
                builder.setDesc("下单鞋子外套178元").setNote("全场75折").setPicture("http://img3.imgtn.bdimg.com/it/u=4271053251,2424464488&fm=21&gp=0.jpg").setShow(1).setTitle("洗衣郎下单").setUrl("www.baidu.com");
                //setShow,是否在访客端显示商品消息。默认为0，即客服能看到此消息，但访客看不到，也不知道该消息已发送给客服。
                source.productDetail = builder.create();

                // 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable(), 如果返回为false，该接口不会有任何动作
                // 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入三个参数分别为来源页面的url，来源页面标题，来源页面额外信息（可自由定义）
                // 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                // 打开回话窗口
                String title = "聊天窗口的标题";
                Unicorn.openServiceActivity(MainActivity.this, // 上下文
                        title, // 聊天窗口的标题
                        source // 咨询的发起来源，包括发起咨询的url，title，描述信息等
                );

                //关闭或显示消息提醒
//                Unicorn.toggleNotification(false);
                // 两个参数分别为访问内容的 uri，和该 uri 对应的简单描述
//                Unicorn.trackUserAccess(uri, description);
            }
        });
    }

    // 添加未读数变化监听，add 为 true 是添加，为 false 是撤销监听。退出界面时，必须撤销，以免造成资源泄露
    private UnreadCountChangeListener mUnreadCountListener = new UnreadCountChangeListener() { // 声明一个成员变量
        @Override
        public void onUnreadCountChange(int count) {
            // 在此更新界面, count 为当前未读数，
//             也可以用 Unicorn.getUnreadCount() 获取总的未读数
            Log.e("","未读消息数=="+count);
        }

        private void addUnreadCountChangeListener(boolean add) {
            Unicorn.addUnreadCountChangeListener(mUnreadCountListener, add);
        }
    };

    @Override
    public void onDestroy() {
        Unicorn.addUnreadCountChangeListener(mUnreadCountListener, false);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //将用户设置为离线，建议关闭长连接savePowerConfig.activeDelay = 0~5000
        Unicorn.setUserInfo(null);
        super.onPause();
    }
}
