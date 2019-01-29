package com.xwy.tao_work.mytaowork.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xwy.tao_work.mytaowork.R;
import com.xwy.tao_work.mytaowork.base.ImageLoaderFactory;
import com.xwy.tao_work.mytaowork.data.User;
import com.xwy.tao_work.mytaowork.login.widget.ChoosePicPopWindow;
import com.xwy.tao_work.mytaowork.mine.widget.ChooseSexPopWindow;
import com.xwy.tao_work.mytaowork.utils.Utils;
import com.xwy.tao_work.mytaowork.widget.MyCircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AlterUserActivity extends AppCompatActivity implements View.OnClickListener{

    private MyCircleImageView iv_alterAvatar;
    private User user;
    private EditText et_alterUserName,et_alterUserAge;
    private TextView tv_alterUseSex;

    /*改变头像变量*/
    private static final String TAG = "UpdataUserActivity";
    private static final int REQUEST_CODE_PICK_IMAGE = 0;           //选择图片
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;       //拍照
    private static final String IMAGE_FILE_NAME = "user_head_icon.jpg";     //拍照之后的图片名称
    private Uri userImageUri;//保存用户头像的uri
    private String path;    //图片的路径

    private ChoosePicPopWindow mpopWindow;  //头像底部菜单
    private ChooseSexPopWindow sexPopWindow;        //性别底部菜单



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_user);
        user = BmobUser.getCurrentUser(User.class);

        //初始化上方标题栏
        Toolbar tl_alterHead = (Toolbar)findViewById(R.id.tl_alterHead);
        tl_alterHead.setTitle("");
        setSupportActionBar(tl_alterHead);
        tl_alterHead.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();

    }

    private void initView(){
        //完成按钮
        Button btn_alterSuccess = findViewById(R.id.btn_alterSuccess);
        btn_alterSuccess.setOnClickListener(this);
        //头像
        iv_alterAvatar = findViewById(R.id.iv_alterAvatar);
        ImageLoaderFactory.getLoader().load(iv_alterAvatar,user.getHead_portrait().getUrl(),R.drawable.default_head,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        //头像
        RelativeLayout rl_alterAvatar = findViewById(R.id.rl_alterAvatar);
        rl_alterAvatar.setOnClickListener(this);
        //性别
        RelativeLayout rl_alterUserSex = findViewById(R.id.rl_alterUserSex);
        rl_alterUserSex.setOnClickListener(this);
        tv_alterUseSex = findViewById(R.id.tv_alterUseSex);
        //密码
        RelativeLayout rl_alterUserPass = findViewById(R.id.rl_alterUserPass);
        rl_alterUserPass.setOnClickListener(this);
        //用户名
        et_alterUserName = findViewById(R.id.et_alterUserName);
        et_alterUserName.setText(user.getUsername());
        //年龄
        et_alterUserAge = findViewById(R.id.et_alterUserAge);
        et_alterUserAge.setText(""+user.getAge());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rl_alterAvatar){
            ChangeAvatar();
        }else if(v.getId() == R.id.rl_alterUserSex){
            ChangeSex();
        }else if(v.getId() == R.id.rl_alterUserPass){
           Intent intent = new Intent(AlterUserActivity.this,UpdatePassword.class);
           startActivity(intent);
        }else if(v.getId() == R.id.btn_alterSuccess){
            final String name = et_alterUserName.getText().toString();
            String age = et_alterUserAge.getText().toString();
            final Integer ageInt = Integer.valueOf(age);
            String sex = tv_alterUseSex.getText().toString();
            final boolean sexUser;
            if(sex.equals("男")){
                sexUser =  false;
            }else {
                sexUser = true;
            }
            if(TextUtils.isEmpty(name)){
                Toast.makeText(AlterUserActivity.this,"请输入用户号",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(age)){
                Toast.makeText(AlterUserActivity.this,"请输年龄",Toast.LENGTH_SHORT).show();
            }else {
                File file = new File(
                        Environment.getExternalStorageDirectory()+File.separator+"MyTaoWork",IMAGE_FILE_NAME);
                if(file.exists()){
                    final BmobFile bmobFile = new BmobFile();
                    user.setHead_portrait(bmobFile);
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                user.setUsername(name);
                                user.setSex(sexUser);
                                user.setAge(ageInt);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(AlterUserActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(AlterUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(AlterUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else {
                    user.setUsername(name);
                    user.setSex(sexUser);
                    user.setAge(ageInt);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(AlterUserActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AlterUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }

    //改变性别
    public void ChangeSex(){
        sexPopWindow = new ChooseSexPopWindow(AlterUserActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.MaleBtn){
                    tv_alterUseSex.setText("男");
                    sexPopWindow.dismiss();
                }else if(v.getId() == R.id.famaleBtn){
                    tv_alterUseSex.setText("女");
                    sexPopWindow.dismiss();
                }else{
                    sexPopWindow.dismiss();
                }
            }
        });
        //显示窗口
        sexPopWindow.showAtLocation(AlterUserActivity.this.findViewById(R.id.btn_alterSuccess)
                , Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    //改变头像
    public void ChangeAvatar(){
        mpopWindow = new ChoosePicPopWindow(AlterUserActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.pickPhotoBtn){
                    Toast.makeText(AlterUserActivity.this,"选择本地照片",Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(AlterUserActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，进行申请
                        ActivityCompat.requestPermissions(AlterUserActivity.this,
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
                            Toast.makeText(AlterUserActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(v.getId() == R.id.takePhotoBtn){
                    Toast.makeText(AlterUserActivity.this,"照相",Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(AlterUserActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(AlterUserActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // 权限还没有授予，进行申请
                        ActivityCompat.requestPermissions(AlterUserActivity.this,
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
        mpopWindow.showAtLocation(AlterUserActivity.this.findViewById(R.id.btn_alterSuccess)
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
            userImageUri = FileProvider.getUriForFile(AlterUserActivity.this,
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

    //intent接受返回的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri imageUri;
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(AlterUserActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
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
                            bm = Utils.getBitmapFormUri(imageUri,AlterUserActivity.this);
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
                    iv_alterAvatar.setImageBitmap(bm);
                    break;
                }


                case REQUEST_CODE_CAPTURE_CAMEIA: {
                    Bitmap bm1 = null;
                    try {
                        bm1 = Utils.getBitmapFormUri(userImageUri,AlterUserActivity.this);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    iv_alterAvatar.setImageBitmap(bm1);
                    break;
                }
            }
        }
    }

}
