package com.example.cnrgamecenterdemo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cnr.common.CnrCallbackListener.LoginProcessListener;
import com.cnr.common.CnrCallbackListener.PayListener;
import com.cnr.platform.CnrPlateform;
import com.cnr.platform.bean.CnrPayInfo;
import com.cnr.platform.bean.NetResult;
import com.unicom.wounipaysms.delegate.RequestDelegate;
import com.unicom.wounipaysms.duandai.WoUniPay;

public class BeginActivity extends Activity {

	private TextView Login;
	private TextView pay;
	private CnrPayInfo payinfo;
	private TextView dxpay;
	private WoUniPay mUniPay;
	private Bundle mBundle;
	int succesed = 0;
	int ordsuccesed = 0;
	int failed = 0;

	private static Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				break;
			case 1:

				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_begin);
		CnrPlateform.getInstance().cnrInit(BeginActivity.this);
		Login = (TextView) findViewById(R.id.login);
		pay = (TextView) findViewById(R.id.pay);
		dxpay = (TextView)findViewById(R.id.dxpay);
		
		
		

		
		
		dxpay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getPAY();
			}
		});
		
		payinfo = new CnrPayInfo();
		payinfo.setGoodsid("1");
		payinfo.setMoney(0.01);
		payinfo.setOrderid("96");
		payinfo.setLoginName("13810616522");
		payinfo.setPassWord("333222");

		pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				CnrPlateform.getInstance().cnrPay(BeginActivity.this, payinfo,
						new PayListener() {
							@Override
							public void finishPay(int flay) {
								// TODO Auto-generated method stub
								switch (flay) {
								case 0:
									Toast.makeText(BeginActivity.this, "付款成功",
											Toast.LENGTH_LONG).show();

									break;
								case -9:
									Toast.makeText(BeginActivity.this, "账号未登录",
											Toast.LENGTH_SHORT).show();
									Intent intent = new Intent();
									intent.setClass(BeginActivity.this,
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

		Login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CnrPlateform.getInstance().cnrLogin(BeginActivity.this,
						new LoginProcessListener() {

							@Override
							public void finishLoginProcess(NetResult result) {
								// TODO Auto-generated method stub
								if (result.getCallbackstatus() == 0) {
									Toast.makeText(BeginActivity.this,
											"TOKEN" + result.getToken(),
											Toast.LENGTH_SHORT).show();
									HttpPost(result.getToken());
									// Intent intent = new Intent();
									// intent.setClass(BeginActivity.this,
									// MainActivity.class);
									// startActivity(intent);
								}
							}
						});
			}
		});
	}

	private void getPAY(){
		int time = (int) (System.currentTimeMillis());
		Timestamp tsTemp = new Timestamp(time);
		String ts = tsTemp.toString();
		
		mUniPay = WoUniPay.getInstance(this);
		mBundle = new Bundle();

		mBundle.putString("itfType", "1");
		mBundle.putString("command", "1");
		mBundle.putString("feetype", "1");
		mBundle.putString("cpId", "13");
		mBundle.putString("serviceId", "15");
		mBundle.putString("channelId", "00");
		mBundle.putString("appId", "00");
		mBundle.putString("myId", "00");
		mBundle.putString("time", ts);
		mBundle.putString("cpcustom", "321");
		mBundle.putString("cporderId", "00000000000000000000000000000000");
		mBundle.putString("tradeName", "交易名称");
		mBundle.putString("price", "0.1元");
		mBundle.putString("servicePhone", "400-10086");
		mBundle.putString("key", "c8f7812dd3b514f659209e1c46506f1a");

		mBundle.putBoolean("showDialog", true);
		
		mUniPay.payAsDuanDai(mBundle, new RequestDelegate() {
			
			@Override
			public void requestSuccessed(String arg0) {
				// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "requestSuccessed"+succesed, Toast.LENGTH_LONG).show();
					succesed++;
			}
			
			@Override
			public void requestOrderSuccessed() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "requestOrderSuccessed"+ordsuccesed, Toast.LENGTH_LONG).show();
				ordsuccesed++;
			}
			
			@Override
			public void requestFailed(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "requestFailed"+failed, Toast.LENGTH_LONG).show();
				failed++;
			}
		});
		mUniPay.sendConfirmSMS();

	}
	


	
	
	
	public void HttpPost(final String token) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpPost httpPost = new HttpPost(url);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("token", token));
				HttpResponse httpResponse = null;
				try {
					// 设置httpPost请求参数
					httpPost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(httpPost);
					// System.out.println(httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 第三步，使用getEntity方法活得返回结果
						String result = EntityUtils.toString(httpResponse
								.getEntity());
						System.out.println("result:------" + result);
						try {
							JSONObject jsonObject = new JSONObject(result);
							int status = jsonObject.getInt("status");
							switch (status) {
							case 1:
								Intent intent = new Intent();
								intent.setClass(BeginActivity.this,
										MainActivity.class);
								startActivity(intent);
								break;
							default:
								Toast.makeText(BeginActivity.this, "账户无效",
										Toast.LENGTH_SHORT).show();
								break;
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private static String url = "http://appplat.test.52ytv.com/activation1/thirdlogin";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			CnrPlateform.getInstance().cnrExit(BeginActivity.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
