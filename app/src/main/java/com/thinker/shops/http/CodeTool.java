package com.thinker.shops.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编解码工具
 * 
 * @author 廖金舟
 * 
 */
public class CodeTool
{
	/**
	 * 将汉字从指定编码转成UTF8编码
	 * 
	 * @param str
	 * @return
	 */
	public static final String toUTF8(String str,String oldcharset)
	{
		try
		{
			return new String(str.getBytes(oldcharset), "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 将汉字从ISO-8859-1编码转成UTF8编码
	 * @param str
	 * @return
	 */
	public static final String toUTF8(String str)
	{
		return toUTF8(str,"ISO-8859-1");
	}

	/**
	 * URL地址参数转码
	 * 
	 * @param str
	 * @return
	 */
	public static final String urlEncoder(String str)
	{
		try
		{
			return URLEncoder.encode(str, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * URL地址参数解码
	 * 
	 * @param str
	 * @return
	 */
	public static final String urlDecoder(String str)
	{
		try
		{
			return URLDecoder.decode(str, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return str;
		}
	}
}
