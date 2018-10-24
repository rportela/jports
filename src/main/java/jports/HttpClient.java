package jports;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {

	private HttpURLConnection connection;
	private final HashMap<String, String> headers = new HashMap<>();
	private final ArrayList<HttpCookie> cookies = new ArrayList<>();
	private URL url;

	public HttpClient setUrl(URL url) {
		this.url = url;
		return this;
	}

	public HttpClient setUrl(String url) throws MalformedURLException {
		return setUrl(new URL(url));
	}

	public URL getUrl() {
		return this.url;
	}

	public String getHeader(String name) {
		return this.headers.get(name);
	}

	public HttpClient setHeader(String name, String value) {
		this.headers.put(name, value);
		return this;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public List<HttpCookie> getCookies() {
		return this.cookies;
	}

	public HttpCookie getCookie(String name) {
		for (int i = 0; i < cookies.size(); i++)
			if (name.equalsIgnoreCase(cookies.get(i).getName()))
				return cookies.get(i);
		return null;
	}
}
