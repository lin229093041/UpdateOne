package com.oracle.oaec.androidproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.oracle.oaec.androidproject.po.Music;
import com.oracle.oaec.androidproject.po.UrlMusic;
import com.oracle.oaec.androidproject.po.User;
import com.oracle.oaec.androidproject.publicmethod.ToUrl;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户点歌历史的Activity
 */
public class DianGeActivity extends AppCompatActivity {
    private BaseAdapter adapter;
    private List<UrlMusic> cp=new ArrayList<>();
    private ImageView img;
    private ListView lv;
    private  String picUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_ge);
         lv= (ListView) findViewById(R.id.lv_diange);
        adapter=new MyAdapter();
        /**
         * 获取头像地址
         */
        FinalHttp http1=new FinalHttp();
        http1.get(ToUrl.Url + "UserServlet2?mode=4&name=" + LoginActivity.Urlusername, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                picUrl=ToUrl.Url+o.toString();

                adapter.notifyDataSetChanged();
                super.onSuccess(o);
            }
        });
        if(lv!=null){
            String s=ToUrl.Url+"MusicServlet?mode=3&user="+LoginActivity.Urlusername;
            FinalHttp http=new FinalHttp();
            http.get(s, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    if(o.toString().equals("失败")){
                        Toast.makeText(DianGeActivity.this,"用户没有点歌历史",Toast.LENGTH_SHORT).show();
                    }else{
                        Gson gson=new Gson();
                        /**
                         * 对获取的数据进行编译（因为歌曲名称可能不符合URL合法字符）
                         */
                        /**
                         * 先判断传来的数据是否为数组
                         */



                            try {
                                cp=gson.fromJson(URLDecoder.decode(o.toString(),"utf-8"), new TypeToken<List<UrlMusic>>() {
                                }.getType());
                                adapter.notifyDataSetChanged();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                        Toast.makeText(DianGeActivity.this,"获取点歌历史成功",Toast.LENGTH_SHORT).show();
                    }
                    super.onSuccess(o);
                }
            });
        }else{
            Toast.makeText(DianGeActivity.this,"点歌lv为空，请检查",Toast.LENGTH_SHORT).show();
        }
        lv.setAdapter(adapter);


    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return cp.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.send_music_item, null);
            TextView musicname = (TextView) view.findViewById(R.id.tv_musicname);
            TextView music_people_name= (TextView) view.findViewById(R.id.lv_music_people_name);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            img= (ImageView) view.findViewById(R.id.music_user_img);
            music_people_name.setText(cp.get(position).getOne_user_name());
            musicname.setText(cp.get(position).getName());
            tv_time.setText(cp.get(position).getTime());

            //解析从网络中获取的图片资源
            DisplayImageOptions displayImageOptions=new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).showImageOnLoading(R.mipmap.ic_launcher).build();
            ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(DianGeActivity.this).diskCacheSize(1024*1024 * 10).memoryCacheSize(1024 * 1024 * 10).build();
            ImageLoader.getInstance().init(config);
            Log.i("1212",picUrl);
            ImageLoader.getInstance().displayImage(picUrl,img,displayImageOptions,new SimpleImageLoadingListener(){
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    //图片加载失败后处理


                }
            });
//            musicname.setText(list.get(position).getFilename());
//            tv_time.setText(list.get(position).getTime());

            return view;
        }
    }
}
