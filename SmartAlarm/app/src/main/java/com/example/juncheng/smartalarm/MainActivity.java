package com.example.juncheng.smartalarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.media.MediaPlayer;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private MyCountDownTimer mc;
    private TextView tv;
    private TextView quiz;
    private Button strt;
    private EditText hours;
    private EditText minutes;
    private EditText _seconds;
    private int hours_sec;
    private int minutes_sec;
    private int _seconds_sec;
    private int seconds;
    private int a, b, sum;
    private MediaPlayer mp;

    private static final int REQUEST_UI = 1;

    private Button answer;
    private TextView tw;
    private BaiduASRDigitalDialog mDialog = null;
    private DialogRecognitionListener mDialogListener = null;
    private String API_KEY = "TbuYqXPGunHjGV6piPyfoMPo";
    private String SECRET_KEY = "b9d6a1023922af2fff4f60fb5090842a";
    VoiceRecognitionConfig config = new VoiceRecognitionConfig();

    public String parse(String str) {                       // found this method in http://blog.csdn.net/u012631267/article/details/19823253
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("zero", 0);
        hm.put("one", 1);
        hm.put("two", 2);
        hm.put("three", 3);
        hm.put("four", 4);
        hm.put("five", 5);
        hm.put("six", 6);
        hm.put("seven", 7);
        hm.put("eight", 8);
        hm.put("nine", 9);
        hm.put("ten", 10);
        hm.put("eleven", 11);
        hm.put("twelve", 12);
        hm.put("thirteen", 13);
        hm.put("fourteen", 14);
        hm.put("fifteen", 15);
        hm.put("sixteen", 16);
        hm.put("seventeen", 17);
        hm.put("eighteen", 18);
        hm.put("nineteen", 19);
        hm.put("twenty", 20);
        hm.put("thirty", 30);
        hm.put("forty", 40);
        hm.put("fifty", 50);
        hm.put("sixty", 60);
        hm.put("seventy", 70);
        hm.put("eighty", 80);
        hm.put("ninety", 90);
        hm.put("hundred", 100);
        hm.put("thousand", 1000);
        hm.put("million", 1000000);
        int i = 0;
        int b = 0;
        int c = 0;
        String[] k = str.split(" ");
        try {
            for (String string : k) {
                if ("hundred".equals(string)) {
                    i *= hm.get("hundred");
                } else if ("thousand".equals(string)) {
                    b = i;
                    b *= hm.get("thousand");
                    i = 0;
                } else if ("million".equals(string)) {
                    c = i;
                    c *= hm.get("million");
                    i = 0;
                } else if ("negative".equals(string)) {
                    i = 0;
                } else {
                    i += hm.get(string);
                }
            }
        } catch (Exception e) {
            b = 0;
            c = 0;
        }

        i += c + b;
        for (String string2 : k) {
            if ("negative".equals(string2)) {
                i = -i;
            }
        }
        String dis = String.valueOf(i);
        return dis;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.victory);
        hours = (EditText) findViewById(R.id.editText);
        minutes = (EditText) findViewById(R.id.editText2);
        _seconds = (EditText) findViewById(R.id.editText3);
        tv = (TextView) findViewById(R.id.textView5);
        quiz = (TextView) findViewById(R.id.textView2);
        strt = (Button) findViewById(R.id.button);
        strt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hours_sec = (Integer.valueOf(hours.getText().toString()).intValue()) * 3600;
                minutes_sec = (Integer.valueOf(minutes.getText().toString()).intValue()) * 60;
                _seconds_sec = Integer.valueOf(_seconds.getText().toString()).intValue();
                seconds = hours_sec + minutes_sec + _seconds_sec;
                mc = new MyCountDownTimer(seconds * 1000+1, 1000);
                mc.start();
                tw.setText("Sulution.");
                tw.setTextColor(0xFFEEEE00);
                quiz.setText("Your Quiz Here!");
            }
        });

        if (mDialog == null) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Bundle params = new Bundle();
            params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
            params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
            params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG);
            params.putString(BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_ENGLISH);
            mDialog = new BaiduASRDigitalDialog(this, params);
            mDialogListener = new DialogRecognitionListener() {
                @Override
                public void onResults(Bundle mResults) {
                    ArrayList<String> rs = mResults != null ? mResults.getStringArrayList(RESULTS_RECOGNITION) : null;
                    if (rs != null && rs.size() > 0) {
                        tw.setText(parse(rs.get(0)));
                        String ssum = String.valueOf(sum);
                        if (parse(rs.get(0)).equals(ssum)) {
                            mp.pause();
                            tw.setTextColor(0xFF00FF00);
                        } else {
                            mp.start();
                            tw.setTextColor(0xFFFF0000);
                        }
                    }
                }
            };
            mDialog.setDialogRecognitionListener(mDialogListener);
        }

        tw = (TextView) findViewById(R.id.textView8);
        answer = (Button) findViewById(R.id.button2);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
                mDialog.show();
            }
        });
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv.setText("Press Answer!");
            a = ranNum();
            b = ranNum();
            sum = a + b;
            quiz.setText(a + " + " + b + " = " + " ? ");
            mp.setLooping(true);
            mp.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("MainActivity", millisUntilFinished + "");
            tv.setText(millisUntilFinished / 1000 + " sec");
        }
    }

    int ranNum() {
        int i = (int) (1 + Math.random() * (50 - 1 + 1));
        return i;
    }
}
