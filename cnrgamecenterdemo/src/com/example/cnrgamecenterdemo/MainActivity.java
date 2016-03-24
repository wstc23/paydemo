package com.example.cnrgamecenterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cnr.common.CnrCallbackListener;
import com.cnr.common.CnrCallbackListener.PayListener;
import com.cnr.common.InitCompleteListener;
import com.cnr.platform.CnrPlateform;
import com.cnr.platform.bean.CnrAppInfo;
import com.cnr.platform.bean.CnrPayInfo;
import com.cnr.platform.bean.NetResult;

public class MainActivity extends Activity {

	private TextView pay;
	private TextView Logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			CnrPlateform.getInstance().cnrExit(MainActivity.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void init() {

//		toolbar = new CnrToolbar(MainActivity.this, 1);
//		toolbar.show();
		
		
		pay = (TextView) findViewById(R.id.payView);
		pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				CnrPayInfo payinfo = new CnrPayInfo();
				payinfo.setGoodsid("1");
				payinfo.setMoney(0.01);
				payinfo.setOrderid("96");
				payinfo.setLoginName("13810616522");
				
				Toast.makeText(MainActivity.this, "付款", 100).show();
				CnrPlateform.getInstance().cnrPay(MainActivity.this, payinfo,new PayListener() {
							@Override
							public void finishPay(int flay) {
								// TODO Auto-generated method stub
								switch (flay) {
								case 0:
									Toast.makeText(MainActivity.this, "付款成功",
											Toast.LENGTH_LONG).show();
									
									break;
								case -9:
									Toast.makeText(MainActivity.this, "账号未登录",
											Toast.LENGTH_SHORT).show();
									Intent intent = new Intent();
									intent.setClass(MainActivity.this,
											BeginActivity.class);
									startActivity(intent);
									break;
								default:
									break;
								}
							}
						});
			}
		});


		CnrAppInfo appInfo = new CnrAppInfo();
		InitCompleteListener initListener = new InitCompleteListener() {

			@Override
			protected void onComplete(int paramInt) {
				if (paramInt == InitCompleteListener.FLAG_SUCCESS) {
					CnrPlateform.getInstance().cnrLogin(MainActivity.this,
							new CnrCallbackListener.LoginProcessListener() {

								@Override
								public void finishLoginProcess(NetResult flag) {

								}

							});
				}

			}

		};
		Logout = (TextView) findViewById(R.id.logout);
		Logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CnrPlateform.getInstance().cnrLogout(MainActivity.this);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BeginActivity.class);
				startActivity(intent);
			}
		});
	}
}
