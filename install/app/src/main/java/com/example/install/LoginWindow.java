package com.example.install;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.pomelo.DataCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/3/31.
 */
public class LoginWindow extends AppCompatActivity implements View.OnClickListener {

    private static final String ARG = "LoginWindow";
    public static LoginWindow instance =null;

    private static final int LOGIN =0;
    private int m_Inlet,m_UserId;//入口
    private EditText m_UserEt,m_PawEt;
    private Button m_RegBut;
    private LinearLayout m_LoginBut;
    private ImageButton m_PasMsgBut;
    private int m_ErrcdInfo=0;
    private String m_Guid;
    private SharedPreferences m_Preferences;
    private SharedPreferences.Editor m_Editor;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN) {
                AlertDialog telDialog = new AlertDialog.Builder(LoginWindow.this).create();
                switch (m_ErrcdInfo){
                    case -1:
                        AppDataManager.GetInstance().setGUID(m_Guid);
                        AppDataManager.GetInstance().setErrcdInfo(m_ErrcdInfo);
                        AppDataManager.GetInstance().setUser(m_UserEt.getText().toString());
                        AppDataManager.GetInstance().setUserid(m_UserId);
                        m_Editor.putString("tel", m_UserEt.getText().toString());
                        m_Editor.putString("pwd", m_PawEt.getText().toString());
                        m_Editor.commit();
                        switch (m_Inlet){
                            case 0:
                                MyInfoWindow.instance.finish();
                                Intent toMyInfoWindow = new Intent(LoginWindow.this,MyInfoWindow.class);
                                startActivity(toMyInfoWindow);
                                finish();
                                break;
                            case 1:
//                                Intent toSanitaryWareWindow = new Intent(LoginWindow.this,SanitaryWareWindow.class);
//                                startActivity(toSanitaryWareWindow);
                                finish();
                                break;
                            default:
                                finish();
                                break;
                        }
                        break;
                    case 101:
                        ToolsManager.GetInstance().setMyToast(LoginWindow.this, "您输入的用户名或密码错误！", 1);
                        break;
                    case 108:
                        telDialog.setTitle("版本提示！");
                        telDialog.setMessage("发现新版本，是否升级！");
                        telDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再说",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        MyClient.GetInstance().getClient().disconnect();
                                        System.exit(0);
                                    }
                                });
                        telDialog.setButton(DialogInterface.BUTTON_POSITIVE, "好的",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
//                                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                                    intent.setData(Uri.parse("http://www.mamakeji.net/mamainstall.apk"));
//                                    startActivity(intent);
                                    }
                                });
                        telDialog.show();
                        break;
                    case 111:
                        telDialog.setTitle("信息提示！");
                        telDialog.setMessage("此账号试用期已过，如需继续使用请在个人信息界面中进行激活操作！！");
                        telDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "再说",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        MyClient.GetInstance().getClient().disconnect();
                                        System.exit(0);
                                    }
                                });
                        telDialog.setButton(DialogInterface.BUTTON_POSITIVE, "好的",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent toMyInfoWindow = new Intent(LoginWindow.this, MyInfoWindow.class);
                                        startActivity(toMyInfoWindow);
                                    }
                                });
                        telDialog.show();
                        break;
                    default:
                        ToolsManager.GetInstance().setMyToast(LoginWindow.this, "登陆失败，请稍后重试！", 1);
                        break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_win);
        instance=this;

        String test_ip = getResources().getString(R.string.app_ip);
        int test_port = Integer.parseInt(getResources().getString(R.string.app_port));
        MyClient.GetInstance().setServerAddre(test_ip, test_port);

        //获取屏幕宽度
        Point p = new Point();
        Display di = this.getWindowManager().getDefaultDisplay();
        di.getSize(p);
        AppDataManager.GetInstance().set_mPoint(p);

        Intent getInlet = getIntent();
        m_Inlet=getInlet.getIntExtra("inlet",-1);

        init();
    }
    private void init(){
        m_Preferences = getSharedPreferences("phone", MODE_PRIVATE);
        m_Editor = m_Preferences.edit();

        m_UserEt = (EditText)findViewById(R.id.login_win_user_et);
        m_PawEt = (EditText)findViewById(R.id.login_win_paw_et);

        m_LoginBut = (LinearLayout)findViewById(R.id.login_win_login_but);
        m_LoginBut.setOnClickListener(this);

        m_RegBut=(Button)findViewById(R.id.reg_but);
        String regButText = getResources().getString(R.string.login_but_text);
        SpannableString ss = new SpannableString(regButText);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.cityFont)),2,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),2,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        m_RegBut.setText(ss);
        m_PasMsgBut=(ImageButton)findViewById(R.id.login_return_but);
        m_PasMsgBut.setOnClickListener(this);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            switch (m_Inlet){
                case 0:
                    finish();
                    break;
                case 1:
                    finish();
                    break;
                default:
                    finish();
                    break;
            }
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_win_login_but:
                Log.i("@@@" + ARG, "你点击了登陆！");
                if (m_UserEt.getText().length()==11 && m_PawEt.getText().length()>5) {
                    MyLogin();
                } else {
                    ToolsManager.GetInstance().setMyToast(this, "账户或密码有误请重新输入！", 1);
                    Intent toRegWindow = new Intent(LoginWindow.this,RegWindow.class);
                    startActivity(toRegWindow);
                }
                break;
            case R.id.login_win_update_pas_but:
//                Log.i("@@@" + ARG, "你点击了忘记密码！");
//                Intent toReg1 = new Intent(LoginWindow.this,RegWindow.class);
//                toReg1.putExtra("regType",1);
//                startActivity(toReg1);
                break;
            case R.id.login_return_but:
                finish();
                break;
            default:
                break;
        }
    }
    /**
     * 登陆
     */
    private void MyLogin(){
        Log.i("@@@" + ARG, "登陆！");
        final String desKey = "12345678";
        String pwd="";
        try {
            pwd = Des.encryptDES(m_PawEt.getText().toString(),desKey);
            Log.i("@@@：" + ARG, "加密后的密码→" + pwd);
            String pwd1 = Des.decryptDES(pwd,desKey);
            Log.i("@@@：" + ARG, "解密后的密码→" + pwd1);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("@@@：" + ARG, "解密后的密码→" + e);
        }
        JSONObject msg = new JSONObject();//一种数据传输方式
        try {
            msg.put("username",m_UserEt.getText().toString());
            msg.put("pas",pwd);
            msg.put("version",Integer.parseInt(getResources().getString(R.string.app_version_code)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("@@@：" + ARG, "发送登陆信息→" + msg);
        m_ErrcdInfo = -1;
        MyClient.GetInstance().getClient().request("connector.userHandler.login",msg, new DataCallBack() {
            @Override
            public void responseData(JSONObject msg) {
                Log.i("@@@：" + ARG, "收到登陆信息→" + msg);
                try {
                    m_ErrcdInfo = msg.getInt("errcd");
                    m_Guid = msg.getJSONObject("data").getString("guid");
                    m_UserId = msg.getJSONObject("data").getInt("Id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what =LOGIN;
                handler.sendMessage(message);
            }
        });
    }
}