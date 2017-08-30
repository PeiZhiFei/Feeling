package com.feifei.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author   裴智飞
 * @date       2014-7-13
 * @date       下午4:05:18
 * @file         MatchUtil.java
 * @content  字符串格式验证，正则表达式相关
 */
public class MatchUtil {

	/**
	 * @param string
	 * @return boolean
	 * @notice 手机号码的验证
	 */
	public static boolean checkPhone(String string) {
		return match("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", string);
	}

	/**
	 * @param string
	 * @return boolean
	 * @notice 邮箱格式的验证
	 */
	public static boolean checkEmail(String string) {
		return match(
				"^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
				string);
	}

	/**
	 * @param string
	 * @return boolean
	 * @notice 快递单号格式的验证
	 */
	public static boolean checkPackage(String string) {
		return match("[\\da-zA-Z]+?", string);
	}

	/**
	 * @param string
	 * @return boolean
	 * @notice 验证字符串是否为网址
	 */
	public static boolean checkHttp(String string) {
		return match(
				"^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
				string);
	}

	private static boolean match(String rule, String string) {
		Pattern pattern = Pattern.compile(rule);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	/**
	 * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
	 * @return 验证身份证号码
	 */
	public static boolean checkIdCard(String idCard) {
		String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
		return Pattern.matches(regex, idCard);
	}

	/**
	 * @param mobile：手机号
	 * @return 验证手机号码
	 */
	public static boolean checkMobile2(String mobile) {
		String regex = "(\\+\\d+)?1[3458]\\d{9}$";
		return Pattern.matches(regex, mobile);
	}

	/**
	 * @param phone：格式：+8602085588447
	 * @return 验证固定电话号码
	 */
	public static boolean checkPhone2(String phone) {
		String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
		return Pattern.matches(regex, phone);
	}

	/**
	 * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
	 * @return 验证空白字符
	 */
	public static boolean checkBlankSpace(String blankSpace) {
		String regex = "\\s+";
		return Pattern.matches(regex, blankSpace);
	}

	/**
	 * @param chinese：中文字符
	 * @return  验证中文
	 */
	public static boolean checkChinese(String chinese) {
		String regex = "^[\u4E00-\u9FA5]+$";
		return Pattern.matches(regex, chinese);
	}

	/**
	 * 
	 * @param birthday：格式：1992-09-03，或1992.09.03
	 * @return 验证日期（年月日）
	 */
	public static boolean checkBirthday(String birthday) {
		String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
		return Pattern.matches(regex, birthday);
	}

	/**
	 * @param postcode 邮政编码
	 * @return 验证邮政编码
	 */
	public static boolean checkPostcode(String postcode) {
		String regex = "[1-9]\\d{5}";
		return Pattern.matches(regex, postcode);
	}

	/**
	 * @param ipAddress IPv4标准地址
	 * @return 验证IP地址
	 */
	public static boolean checkIpAddress(String ipAddress) {
		String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
		return Pattern.matches(regex, ipAddress);
	}

}
