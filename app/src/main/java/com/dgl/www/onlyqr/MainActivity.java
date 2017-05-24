package com.dgl.www.onlyqr;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgl.www.onlyqr.zxing.android.CaptureActivity;
import com.google.zxing.WriterException;


public class MainActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener {

    private Button scanBtn;
    private EditText resultTv;
    private TextView desc;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    protected int mScreenWidth ;
    private ImageView iv_qr_image;
    private static final int REQUEST_CODE_SCAN = 0x0000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    private void initView() {
        scanBtn = (Button) findViewById(R.id.scanBtn);
        resultTv = (EditText) findViewById(R.id.resultTv);
        desc = (TextView) findViewById(R.id.desc);
        iv_qr_image = (ImageView) findViewById(R.id.iv_qr_image);
        scanBtn.setOnClickListener(this);
        resultTv.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanBtn:

                Intent intent = new Intent(MainActivity.this,
                        CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
//                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                desc.setVisibility(View.VISIBLE);

//                StringTokenizer st = new StringTokenizer(content,";");
                String showStr = "";
                String[] sourceStrArray = content.split(";");
                for (int i = 0; i < sourceStrArray.length; i++) {
                    if(i==0){
                        showStr +=  sourceStrArray[i];
                    }else {
                        showStr += "\n\r" + sourceStrArray[i];
                    }
                }
//                while(st.hasMoreTokens() ){
//                    showStr += "\n\r" + st.nextToken();
//                }
                resultTv.setText(showStr);
            }
        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.resultTv:

                ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(resultTv.getText().toString().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                cmb.getText();//获取粘贴信息
                showToast("已复制结果到粘贴板");
                break;
        }
        return false;
    }

    //生成二维码
    public void Create2QR(View v){
        String uri = resultTv.getText().toString();
        if(TextUtils.isEmpty(uri)){
            showToast("填写内容为空");
        }else {
            Bitmap bitmap;
            try {
                bitmap = BitmapUtil.createQRCode(uri, mScreenWidth);
                if(bitmap != null){
                    iv_qr_image.setImageBitmap(bitmap);
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

    }


    //复制二维码
    public void copyStr(View v){
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(resultTv.getText().toString().trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        cmb.getText();//获取粘贴信息
        showToast("已复制结果到粘贴板");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAll();
    }
}
