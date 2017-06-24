package com.cdw.zhihutopnews.localfragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.bean.AddFileInfo;
import com.cdw.zhihutopnews.uitls.ACache;
import com.cdw.zhihutopnews.uitls.OpenFile;
import com.cdw.zhihutopnews.uitls.ProgressDialog;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created byAdministrator on 2017/4/18.
 * 本地文档子界面
 */

public class LocalTabAll extends LazyFragment {
    private View view;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    public ProgressDialog dialog;
    private ListView mListview;

    private List<AddFileInfo> list = new ArrayList<AddFileInfo>();
    private String filePath = Environment.getExternalStorageDirectory().toString() + File.separator;
    private Adapter adapter;
    private ACache aCache;
    private String fileDate = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_local, container, false);

        mListview = (ListView) view.findViewById(R.id.listview);
        aCache = ACache.get(getActivity());
        onLoad();

        initUI();//实例化控件
        isPrepared = true;
        lazyLoad();//加载数据

        return view;
    }

    /**
     * 实例化组件
     */
    private void initUI() {

    }

    /**
     * 实现懒加载,当屏幕显示这个界面的时候才会触发次方法
     */
    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            //TODO 在这里处理逻辑
            Toast.makeText(getContext(), "LocalTabAll", Toast.LENGTH_SHORT).show();
        }
    }
    public void onLoad() {
        adapter = new Adapter(getActivity());
        String string = aCache.getAsString("file");
        if (string == null) {
            showProgress();
            new MyThread().start();
        } else {
            String[] str = string.split(",");

            for (int i = 0; i < str.length; i++) {
                Log.i("file", str[i]);
                File f = new File(str[i]);
                if (f.exists()) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(f);
                        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(f.lastModified()));
                        AddFileInfo info = new AddFileInfo(f.getName(), Long.valueOf(fis.available()), time, false, f.getAbsolutePath());
                        fileDate += f.getAbsolutePath() + ",";
                        list.add(info);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }
        mListview.setOnItemClickListener(onItemClickListener);
        mListview.setAdapter(adapter);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(OpenFile.openFile(list.get(position).getPath()));
        }
    };


    public class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                doSearch(filePath);
                Thread.sleep(2000);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = 1;
                handler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                dismissProgress();
                adapter.notifyDataSetChanged();
                aCache.put("file", fileDate.substring(0, (fileDate.length() - 1)), 600);
            }
        }
    };


    /****
     *计算文件大小
     * @param length
     * @return
     */
    public static String ShowLongFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }


    /****
     * 递归算法获取本地文件
     * @param path
     */
    private void doSearch(String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                if(!(fileArray.length >0)) {
                    return ;
                }
                for (File f : fileArray) {
                    if (f.isDirectory()) {
                        doSearch(f.getPath());
                    } else {
                        if (f.getName().endsWith(".ppt") || f.getName().endsWith(".pptx") || f.getName().endsWith(".docx")
                                || f.getName().endsWith(".xls") || f.getName().endsWith(".doc") || f.getName().endsWith(".txt") || f.getName().endsWith(".pdf")) {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(f);
                                String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(f.lastModified()));
                                AddFileInfo info = new AddFileInfo(f.getName(), Long.valueOf(fis.available()), time, false, f.getAbsolutePath());
                                list.add(info);
                                fileDate += f.getAbsolutePath() + ",";
                                Log.i("url", f.getAbsolutePath() + "--" + f.getName() + "---" + fis.available() + "--");
                                System.out.println("文件名称：" + f.getName());
                                System.out.println("文件是否存在：" + f.exists());
                                System.out.println("文件的相对路径：" + f.getPath());
                                System.out.println("文件的绝对路径：" + f.getAbsolutePath());
                                System.out.println("文件可以读取：" + f.canRead());
                                System.out.println("文件可以写入：" + f.canWrite());
                                System.out.println("文件上级路径：" + f.getParent());
                                System.out.println("文件大小：" + f.length() + "B");
                                System.out.println("文件最后修改时间：" + new Date(f.lastModified()));
                                System.out.println("是否是文件类型：" + f.isFile());
                                System.out.println("是否是文件夹类型：" + f.isDirectory());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    class Adapter extends BaseAdapter {
        private int[] img_word = new int[]{R.drawable.documents_icon_doc, R.drawable.documents_icon_xls, R.drawable.documents_icon_ppt, R.drawable.documents_icon_pdf, R.drawable.documents_icon_text, R.mipmap.ic_launcher};
        private LayoutInflater inflater;

        public Adapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_mytask_file_listview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AddFileInfo info = (AddFileInfo) getItem(position);
            if (info.getName().endsWith(".doc") || info.getName().endsWith(".docx")) {
                holder.iv_img.setImageResource(img_word[0]);
            } else if (info.getName().endsWith(".xls")) {
                holder.iv_img.setImageResource(img_word[1]);
            } else if (info.getName().endsWith(".ppt") || info.getName().endsWith(".pptx")) {
                holder.iv_img.setImageResource(img_word[2]);
            } else if (info.getName().endsWith(".pdf")) {
                holder.iv_img.setImageResource(img_word[3]);
            } else if (info.getName().endsWith(".txt")) {
                holder.iv_img.setImageResource(img_word[4]);
            } else {
                holder.iv_img.setImageResource(img_word[5]);
            }
            holder.tv_name.setText(info.getName());
            holder.size.setText(ShowLongFileSzie(info.getSize()));
            holder.time.setText(info.getTime());
            return convertView;
        }


    }

    class ViewHolder {

        private ImageView iv_img;
        private TextView tv_name;
        private TextView size;
        private TextView time;

        public ViewHolder(View view) {
            iv_img = (ImageView) view.findViewById(R.id.item_file_img);
            tv_name = (TextView) view.findViewById(R.id.item_file_name);
            size = (TextView) view.findViewById(R.id.item_file_size);
            time = (TextView) view.findViewById(R.id.item_file_time);
        }
    }


    /***
     * 启动
     */
    public void showProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
        }
        dialog.showMessage("正在加载");
    }

    /***
     * 关闭
     */
    public void dismissProgress() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
        }
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

