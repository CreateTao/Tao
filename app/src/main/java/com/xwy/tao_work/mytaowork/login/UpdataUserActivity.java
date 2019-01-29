package com.xwy.tao_work.mytaowork.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.login.widget.ChoosePicPopWindow;
import com.xwy.tao_work.mytaowork.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UpdataUserActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UpdataUserActivity";
    private static final int REQUEST_CODE_PICK_IMAGE = 0;           //选择图片
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;       //拍照
    private static final String IMAGE_FILE_NAME = "user_head_icon.jpg";     //拍照之后的图片名称
    private Uri userImageUri;//保存用户头像的uri
    private String path;    //图片的路径

    private ImageView iv_aragar;
    private EditText et_userName,et_age;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private Spinner sp_master;


    private ChoosePicPopWindow mpopWindow;
    private ArrayList<String> list = new ArrayList<>();
    private boolean sex = false;        //性别判断，false为男,true为女

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatauser);

        //初始化上方标题栏
        Toolbar tl_UpdataUser_head = (Toolbar)findViewById(R.id.tl_UpdataUser_head);
        tl_UpdataUser_head.setTitle("");
        setSupportActionBar(tl_UpdataUser_head);
        //初始化控件
        initView();
    }

    private void initView(){
        iv_aragar = findViewById(R.id.iv_aragar);
        iv_aragar.setOnClickListener(this);

        et_userName = findViewById(R.id.et_userName);
        et_age = findViewById(R.id.et_age);

        rb_male = findViewById(R.id.rb_male);
        rb_male.setChecked(true);
        RadioButton rb_female = findViewById(R.id.rb_female);
        rg_sex = findViewById(R.id.rg_sex);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int maleID = rb_male.getId();
                if(checkedId == maleID){
                    sex = false;
                    Toast.makeText(UpdataUserActivity.this,"您选择的是男",Toast.LENGTH_SHORT).show();
                }else{
                    sex = true;
                    Toast.makeText(UpdataUserActivity.this,"您选择的是女",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btn_upData = findViewById(R.id.btn_upData);
        btn_upData.setOnClickListener(this);

        sp_master = findViewById(R.id.sp_master);
        list.add("新手");
        list.add("办公软件应用");
        list.add("多媒体设计");
        list.add("网站软件开发");
        list.add("其他");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_master.setAdapter(adapter);
        sp_master.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UpdataUserActivity.this,"您选择的是"+list.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_aragar){
            chooseImgWay();
        }else if(v.getId() == R.id.btn_upData){
            //判断输入的问题
            getTrue();
        }
    }

    private void getTrue(){
        final String userName = et_userName.getText().toString();
        String age = et_age.getText().toString();
        final String master = list.get(sp_master.getSelectedItemPosition());
        Drawable drawable = iv_aragar.getDrawable();
        //;
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(UpdataUserActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(age)){
            Toast.makeText(UpdataUserActivity.this,"年龄不能为空",Toast.LENGTH_SHORT).show();
        }else if(drawable == null){
            Toast.makeText(UpdataUserActivity.this,"图片不能为空",Toast.LENGTH_SHORT).show();
        }else{
            final Integer ageInt = Integer.valueOf(et_age.getText().toString());
            Log.i(TAG, "年龄："+ageInt);
            final User user = BmobUser.getCurrentUser(User.class);
            Log.i(TAG, "当前登录用户ID："+user.getObjectId());
            final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory()+File.separator+"MyTaoWork",IMAGE_FILE_NAME));
            user.setHead_portrait(bmobFile);
            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        user.setUsername(userName);
                        user.setSex(sex);
                        user.setMaster_computer(master);
                        user.setAge(ageInt);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(UpdataUserActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent("com.xwy.tao_work_mytaowork.FORCE_ONLINE");
                                    sendBroadcast(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UpdataUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(UpdataUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
            Log.i(TAG, "path:"+userImageUri.getPath());

        }
    }

    //intent接受返回的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri imageUri;
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(UpdataUserActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
        } else if(resultCode == RESULT_OK){
            switch (requestCode) {
                //相册照片选择
                case REQUEST_CODE_PICK_IMAGE:{
                    //返回选择图片的uri
                    imageUri = data.getData();
                    userImageUri = imageUri;
                    //通过uri的方式返回，部分手机uri可能为空
                    Bitmap bm = null;
                    if(imageUri != null){
                        try {
                            //将获得的图片uri进行压缩，不然图片像素过大，可能会出现错误
                            bm = Utils.getBitmapFormUri(imageUri,UpdataUserActivity.this);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        //部分手机可能直接存放在bundle中
                        Bundle bundleExtras = data.getExtras();
                        if(bundleExtras != null){
                            bm = bundleExtras.getParcelable("data");
                        }
                    }
                    iv_aragar.setImageBitmap(bm);
                    break;
                }


                case REQUEST_CODE_CAPTURE_CAMEIA: {
                    Bitmap bm1 = null;
                    try {
                        bm1 = Utils.getBitmapFormUri(userImageUri,UpdataUserActivity.this);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    iv_aragar.setImageBitmap(bm1);

                    break;
                }
            }
        }
    }

    //选择图片的方式，从相册或拍摄
    private void chooseImgWay(){
        mpopWindow = new ChoosePicPopWindow(UpdataUserActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.pickPhotoBtn){
                    Toast.makeText(UpdataUserActivity.this,"选择本地照片",Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(UpdataUserActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，进行申请
                        ActivityCompat.requestPermissions(UpdataUserActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
                    } else {
                        // 如果权限已经申请过，直接进行图片选择
                        mpopWindow.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        // 判断系统中是否有处理该 Intent 的 Activity
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                        } else {
                            Toast.makeText(UpdataUserActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(v.getId() == R.id.takePhotoBtn){
                    Toast.makeText(UpdataUserActivity.this,"照相",Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(UpdataUserActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(UpdataUserActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，进行申请
                        ActivityCompat.requestPermissions(UpdataUserActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300); // 申请的 requestCode 为 300
                    } else {
                        // 权限已经申请，直接拍照
                        mpopWindow.dismiss();
                        imageCapture();
                    }
                }else if(v.getId() == R.id.cancelBtn){
                    mpopWindow.dismiss();
                }
            }
        });
        //显示窗口
        mpopWindow.showAtLocation(UpdataUserActivity.this.findViewById(R.id.lin_Updata)
                , Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    //跳转到拍照，拍完并intent发送请求
    private void imageCapture() {
        Intent intent;
        //getMyPetRootDirectory()得到的是Environment.getExternalStorageDirectory() + File.separator+"MyPet"
        //也就是我之前创建的存放头像的文件夹（目录）
        File pictureFile = new File(Environment.getExternalStorageDirectory()+File.separator+"MyTaoWork", IMAGE_FILE_NAME);
        if(!pictureFile.getParentFile().exists()){
            pictureFile.getParentFile().mkdirs();
        }
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //这一句非常重要
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //Android 7.0及以上获取文件 Uri
            //""中的内容是随意的，但最好用package名.provider名的形式，清晰明了
            userImageUri = FileProvider.getUriForFile(UpdataUserActivity.this,
                    "com.xwy.tao_work.mytaowork.login", pictureFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            userImageUri = Uri.fromFile(pictureFile);
        }
        // 去拍照,拍照的结果存到oictureUri对应的路径中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, userImageUri);
        Log.e(TAG,"before take photo"+userImageUri.toString());
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
    }
}
