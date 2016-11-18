package com.thinker.shops.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpClient
{
	private Map<String, String>	httpHeaders			= null;
	private int					connectionTimeout	= 20000;
	private int					readTimeout			= 120000;

	public void setHttpHeaders(Map<String, String> httpHeaders)
	{
		this.httpHeaders = httpHeaders;
	}

	public void addHttpHeaders(Map<String, String> httpHeaders)
	{
		if (this.httpHeaders != null)
		{
			System.arraycopy(httpHeaders, 0, this.httpHeaders, this.httpHeaders.size(), httpHeaders.size());
		}
		else
		{
			this.httpHeaders = httpHeaders;
		}
	}

	public void addHttpHeaders(String key, String value)
	{
		if (this.httpHeaders != null)
		{
			this.httpHeaders.put(key, value);
		}
		else
		{
			this.httpHeaders = new HashMap<String, String>();
			this.httpHeaders.put(key, value);
		}
	}

	public String getString(String URL) throws HttpClientException
	{
		byte[] byteData = getData(URL);

		if (byteData != null && byteData.length > 0)
		{
			try
			{
				return new String(byteData, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		return "";
	}

	public String getString(String URL, Map<String, Object> formParameters) throws HttpClientException
	{
		byte[] byteData = getData(URL, formParameters);

		if (byteData != null && byteData.length > 0)
		{
			try
			{
				return new String(byteData, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		return "";
	}

	public String getString(String URL, String postData) throws HttpClientException
	{
		byte[] byteData = getData(URL, postData);

		if (byteData != null && byteData.length > 0)
		{
			try
			{
				return new String(byteData, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		return "";
	}
	
	public String getString(String URL, byte[] postData) throws HttpClientException
	{
		byte[] byteData = getData(URL, postData);

		if (byteData != null && byteData.length > 0)
		{
			try
			{
				return new String(byteData, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		return "";
	}

	public byte[] getData(String urlString) throws HttpClientException
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			//conn.setRequestMethod("GET");
			//conn.setConnectTimeout(connectionTimeout);
			//conn.setReadTimeout(readTimeout);
			//conn.setDoInput(true);
			//conn.setDoOutput(true);
			conn.connect();

			if (httpHeaders != null && httpHeaders.size() > 0)
			{
				Set<String> headers = httpHeaders.keySet();
				for (Iterator<String> it = headers.iterator(); it.hasNext();)
				{
					String headerName = it.next();
					String headerValue = httpHeaders.get(headerName);
					if (headerValue != null && headerValue.trim().length() > 0)
					{
						conn.addRequestProperty(headerName, headerValue);
					}
				}
			}

			InputStream stream = conn.getInputStream();
			byte[] data = readUrlStream(stream);
			conn.disconnect();

			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new HttpClientException( e.getMessage());
		}
	}
	
	public byte[] getData(String urlString, byte[] postData) throws HttpClientException
	{
		try
		{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setConnectTimeout(connectionTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (httpHeaders != null && httpHeaders.size() > 0)
			{
				Set<String> headers = httpHeaders.keySet();
				for (Iterator<String> it = headers.iterator(); it.hasNext();)
				{
					String headerName = it.next();
					String headerValue = httpHeaders.get(headerName);

					if (headerValue != null && headerValue.trim().length() > 0)
					{
						conn.addRequestProperty(headerName, headerValue);
					}
				}
			}

			DataOutputStream out = null;
			try
			{
				out = new DataOutputStream(conn.getOutputStream()); 

				if (postData != null && postData.length > 0)
				{
					out.write(postData);
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new HttpClientException(  e.getMessage());
			}
			finally
			{
				if (out != null)
				{
					out.flush();
					out.close();
				}
			}

			if (conn.getResponseCode() != 200)
			{
				throw new HttpClientException("服务器无法正确响应。");
			}

			InputStream stream = conn.getInputStream();
			byte[] data = readUrlStream(stream);
			conn.disconnect();

			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new HttpClientException( e.getMessage());
		}
	}

	public byte[] getData(String urlString, String postData) throws HttpClientException
	{
		try
		{
			return getData(urlString,postData.getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			
			throw new HttpClientException( e.getMessage());
		}
	}

	public byte[] getData(String urlString, Map<String, Object> formParameters) throws HttpClientException
	{
		String postData = "";

		if (formParameters != null && formParameters.size() > 0)
		{
			Set<String> keies = formParameters.keySet();
			Iterator<String> it = keies.iterator();
			StringBuffer buf = new StringBuffer();

			for (int paramCount = 0; it.hasNext();)
			{
				String parameterName = it.next();
				Object parameterValue = formParameters.get(parameterName);

				if (parameterValue == null)
				{
					continue;
				}

				if (parameterValue instanceof String)
				{
					if (parameterValue.toString().equalsIgnoreCase("null"))
					{
						continue;
					}
				}

				if (parameterValue instanceof String[])
				{
					String[] strTemp = (String[]) parameterValue;
					for (String s : strTemp)
					{
						parameterValue = CodeTool.urlEncoder(s);
						if (paramCount > 0)
						{
							buf.append("&");
						}
						buf.append(parameterName);
						buf.append("=");
						buf.append(parameterValue);
						++paramCount;
					}
				}
				else
				{
					parameterValue = CodeTool.urlEncoder(String.valueOf(parameterValue));
					if (paramCount > 0)
					{
						buf.append("&");
					}
					buf.append(parameterName);
					buf.append("=");
					buf.append(parameterValue);
					++paramCount;
				}
			}

			postData = buf.toString();
		}

		return getData(urlString, postData);
	}

	private byte[] readUrlStream(InputStream networkStream) throws HttpClientException, IOException
	{
		byte[] bytes = new byte[100];
		byte[] bytecount = null;
		int n = 0;
		int ilength = 0;
		try
		{
			while ((n = networkStream.read(bytes)) >= 0)
			{
				if (bytecount != null)
				{
					ilength = bytecount.length;
				}
				byte[] tempbyte = new byte[ilength + n];
				if (bytecount != null)
				{
					System.arraycopy(bytecount, 0, tempbyte, 0, ilength);
				}

				System.arraycopy(bytes, 0, tempbyte, ilength, n);
				bytecount = tempbyte;

				if (n == -1)
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new HttpClientException("读取网络流时出现异常：" + e.getMessage());
		}
		finally
		{
			networkStream.close();
		}
		return bytecount;
	}

	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	public void setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
	}

	public int getReadTimeout()
	{
		return readTimeout;
	}
}