package jports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * This class wraps useful methods for handling URL connections and
 * sending/receiving data from HTTP transport;
 * 
 * @author rportela
 *
 */
public class HttpClient {

	private static final Gson GSON = new GsonBuilder().create();
	private static final DocumentBuilderFactory XML_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
	private HttpURLConnection connection;
	private final HashMap<String, String> requestHeaders = new HashMap<>();
	private final ArrayList<HttpCookie> cookies = new ArrayList<>();
	private URL url;
	private String method = "GET";
	private byte[] payload;
	private Charset charset = Charset.forName("UTF-8");
	private Map<String, List<String>> responseHeaders;
	private int redirectCount;
	private int redirectMax = 10;

	/**
	 * Creates a new instance of the HTTP client with the default value of specific
	 * headers;
	 */
	public HttpClient() {
		requestHeaders.put("Cache-Control", "no-cache");
		requestHeaders.put("User-Agent", "JPorts Http Client 2.0");
		requestHeaders.put("Connection", "keep-alive");
	}

	/**
	 * Sets the URL of the HTTP client;
	 * 
	 * @param url
	 * @return
	 */
	public HttpClient setUrl(URL url) {
		this.url = url;
		this.redirectCount = 0;
		return this;
	}

	/**
	 * Gets the current URL of this HTTP client;
	 * 
	 * @return
	 */
	public URL getUrl() {
		return this.url;
	}

	/**
	 * Sets the current URL of this HTTP client;
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public HttpClient setUrl(String url) throws MalformedURLException {
		return setUrl(new URL(url));
	}

	/**
	 * Gets a specific header of this HTTP client;
	 * 
	 * @param name
	 * @return
	 */
	public String getRequestHeader(String name) {
		return this.requestHeaders.get(name);
	}

	/**
	 * Sets a specific header of this HTTP client;
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public HttpClient setRequestHeader(String name, String value) {
		this.requestHeaders.put(name, value);
		return this;
	}

	/**
	 * Gets all the headers of this HTTP client;
	 * 
	 * @return
	 */
	public Map<String, String> getRequestHeaders() {
		return this.requestHeaders;
	}

	/**
	 * Gets a list of all coolies in this HTTP client;
	 * 
	 * @return
	 */
	public List<HttpCookie> getCookies() {
		return this.cookies;
	}

	/**
	 * Gets a specific cookie by name from this HTTP client;
	 * 
	 * @param name
	 * @return
	 */
	public HttpCookie getCookie(String name) {
		for (int i = 0; i < cookies.size(); i++)
			if (name.equalsIgnoreCase(cookies.get(i).getName()))
				return cookies.get(i);
		return null;
	}

	/**
	 * Gets the HTTP method currently set on this HTTP client;
	 * 
	 * @return
	 */
	public String getMethod() {
		return this.method;
	}

	/**
	 * Sets a specific HTTP method on this client;
	 * 
	 * @param method
	 * @return
	 */
	public HttpClient setMethod(String method) {
		this.method = method;
		return this;
	}

	/**
	 * Gets the pay load that will be sent through the HTTP transport;
	 * 
	 * @return
	 */
	public byte[] getPayload() {
		return this.payload;
	}

	/**
	 * Sets the pay load that will be sent through the HTTP transport;
	 * 
	 * @param payload
	 * @return
	 */
	public HttpClient setPayload(String contentType, byte[] payload) {
		this.requestHeaders.put("Content-Type", contentType);
		this.payload = payload;
		return this;
	}

	/**
	 * Sets the content type header to application/x-www-form-urlencoded and puts
	 * the URL encoded key value pairs of the form as encoded bytes in the pay load
	 * array;
	 * 
	 * @param form
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public HttpClient setPayload(Map<String, String> form) throws UnsupportedEncodingException {
		String charsetName = charset.name();
		StringBuilder postBuilder = new StringBuilder(1024);
		boolean prepend_amp = false;
		for (Entry<String, String> entry : form.entrySet()) {
			if (prepend_amp) {
				postBuilder.append('&');
			} else {
				prepend_amp = true;
			}
			String name = entry.getKey();
			String value = entry.getValue();
			postBuilder.append(name);
			postBuilder.append('=');
			if (value != null && value.length() > 0) {
				postBuilder.append(URLEncoder.encode(value, charsetName));
			}
		}

		return setPayload(
				"application/x-www-form-urlencoded",
				postBuilder.toString().getBytes(charset));
	}

	/**
	 * Gets the number of redirects that the current HTTP client has followed. This
	 * number should not exceed "redirectMax";
	 * 
	 * @return
	 */
	public int getRedirectCount() {
		return this.redirectCount;
	}

	/**
	 * Gets the maximum number of redirects that this HTTP client should follow.
	 * This setting is here to prevent infinite redirect loops.
	 * 
	 * @return
	 */
	public int getRedirectMax() {
		return this.redirectMax;
	}

	/**
	 * Set the maximum number of redirects that this HTTP client should follow. This
	 * setting is here to prevent infinite redirect loops.
	 * 
	 * @param max
	 * @return
	 */
	public HttpClient setRedirectMax(int max) {
		this.redirectMax = max;
		return this;
	}

	/**
	 * Gets the value of a response header field. Note that this method does not
	 * makes the connection if needed. Instead, it returns null as the header value;
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getResponseHeader(String name) {
		return this.responseHeaders == null
				? null
				: this.responseHeaders.get(name);
	}

	/**
	 * Gets the names of all response headers. Note that this method does not makes
	 * the connection if needed. Instead, it returns null;
	 * 
	 * @return
	 */
	public Set<String> getResponseHeaderNames() {
		return this.responseHeaders == null
				? null
				: this.responseHeaders.keySet();
	}

	/**
	 * Gets the response header mapping of name to list of strings. Or Null, if no
	 * connection was made;
	 * 
	 * @return
	 */
	public Map<String, List<String>> getResponseHeaders() {
		return this.responseHeaders;
	}

	/**
	 * Creates a URLConnection instance that represents a connection to the remote
	 * object referred to by the URL. A new instance of URLConnection is created
	 * every time when invoking the URLStreamHandler.openConnection(URL) method of
	 * the protocol handler for this URL. This method will also call the
	 * URLConnection.connect() to retrieve data or may use getOutputStream() to
	 * write values to the target.
	 * 
	 * @return
	 * @throws IOException
	 */
	public HttpClient connect() throws IOException {
		connection = (HttpURLConnection) this.url.openConnection();
		connection.setInstanceFollowRedirects(true);
		processRequestHeaders();
		processRequestCookies();
		processPayload();
		responseHeaders = connection.getHeaderFields();
		processContentType();
		processResponseCookies();
		processRedirects();
		payload = null;
		return this;
	}

	/**
	 * Indicates that other requests to the server are unlikely in the near future.
	 * Calling disconnect() should not imply that this HttpURLConnection instance
	 * can be reused for other requests so we set our pointer to null to force the
	 * instantiation of another
	 */
	public void disconnect() {
		if (this.connection != null) {
			this.connection.disconnect();
			this.connection = null;
		}
	}

	/**
	 * This method joins cookie name, value pairs into a single string and puts that
	 * under the corresponding "Cookie" property of the request;
	 */
	private void processRequestCookies() {
		if (!cookies.isEmpty()) {
			StringBuilder builder = new StringBuilder(250);
			boolean prependComma = false;
			for (HttpCookie cookie : cookies) {
				if (prependComma)
					builder.append("; ");
				else
					prependComma = true;

				builder.append(cookie.getName());
				builder.append("=");
				builder.append(cookie.getValue());
			}
			connection.setRequestProperty("Cookie", builder.toString());
		}
	}

	/**
	 * This method locates "Set-Cookie" headers in the response, parses them and
	 * either append to the cookies collection or updates it with the newly received
	 * when the name matches.
	 */
	private void processResponseCookies() {
		List<HttpCookie> parsedCookies = HttpCookie.parse(connection.getHeaderField("Set-Cookie"));
		for (HttpCookie hc : parsedCookies) {
			boolean replaced = false;
			for (int i = 0; i < cookies.size() && replaced == false; i++) {
				if (hc.getName().equalsIgnoreCase(cookies.get(i).getName())) {
					cookies.set(i, hc);
					replaced = true;
				}
			}
			if (!replaced) {
				cookies.add(hc);
			}
		}
	}

	/**
	 * Sets the request method and puts the request headers as request properties in
	 * the underlying HTTP URL connection;
	 * 
	 * @throws ProtocolException
	 */
	private void processRequestHeaders() throws ProtocolException {
		connection.setRequestMethod(method);
		for (Entry<String, String> header : requestHeaders.entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
	}

	/**
	 * Either uploads the pay load if not null or just makes a connection to the
	 * remote part.
	 * 
	 * @throws IOException
	 */
	private void processPayload() throws IOException {
		if (this.payload != null) {
			connection.setDoOutput(true);
			try (OutputStream os = connection.getOutputStream()) {
				os.write(this.payload);
				os.flush();
				os.close();
			}
		} else {
			connection.connect();
		}
	}

	/**
	 * Detects specific redirect HTTP response codes and makes the connection to the
	 * indicated location. Note that this method will throw an exception if the
	 * number of redirects being followed exceeds the setting in "redirectMax";
	 * 
	 * @throws IOException
	 */
	private void processRedirects() throws IOException {
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
				responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
				responseCode == HttpURLConnection.HTTP_SEE_OTHER) {

			if (redirectCount > redirectMax) {
				throw new RuntimeException("Too many redirects detected: " + redirectCount + " -> " + url);
			}
			redirectCount++;
			requestHeaders.put("Referer", url.toString());
			url = new URL(connection.getHeaderField("Location"));
			connect();
		}
	}

	/**
	 * Uses the content type header of the response to identify the character set
	 * being used on the server;
	 */
	private void processContentType() {
		String contentType = connection.getContentType();
		int ppos = contentType.indexOf("charset=") + 8;
		if (ppos > 7) {
			String charsetName = contentType.substring(ppos);
			charset = Charset.forName(charsetName);
		}
	}

	/**
	 * Gets the response code returned by the remote object;
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getResponseCode() throws IOException {
		if (this.connection == null)
			this.connect();
		return this.connection.getResponseCode();
	}

	/**
	 * Gets the content length returned by the remote object;
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getContentLength() throws IOException {
		if (this.connection == null)
			this.connect();
		return this.connection.getContentLength();
	}

	/**
	 * Gets the content type returned by the remote object;
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getContentType() throws IOException {
		if (this.connection == null)
			this.connect();
		return this.connection.getContentType();
	}

	/**
	 * Gets the input stream of the connection to read bytes from it;
	 * 
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException {
		if (this.connection == null)
			this.connect();
		return this.connection.getInputStream();
	}

	/**
	 * Gets the response from the remote object as an array of bytes;
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] getResponseBytes() throws IOException {
		try (InputStream is = getInputStream()) {
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream(4096)) {
				byte[] buffer = new byte[4096];
				int read;
				while ((read = is.read(buffer)) >= 0) {
					bos.write(buffer, 0, read);
				}
				byte[] output = bos.toByteArray();
				bos.close();
				return output;
			} finally {
				is.close();
			}
		}
	}

	/**
	 * Gets the response from the remote object as String encoded with the character
	 * set defined in the content type header;
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getResponseText() throws IOException {
		try (InputStreamReader reader = new InputStreamReader(getInputStream(), charset)) {
			StringBuilder builder = new StringBuilder(4096);
			try {
				char[] buffer = new char[4096];
				int read;
				while ((read = reader.read(buffer)) >= 0) {
					builder.append(buffer, 0, read);
				}
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return builder.toString();
		}
	}

	/**
	 * Uses GSON to parse the response text as JSON encoding of a specific class;
	 * 
	 * @param classOfT
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public <T> T getResponseJson(Class<T> classOfT) throws JsonSyntaxException,
			IOException {
		return GSON.fromJson(getResponseText(), classOfT);
	}

	/**
	 * Uses GSON to parse the response text as JSON encoding of a specific java
	 * type;
	 * 
	 * @param ofType
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public <T> T getResponseJson(Type ofType) throws JsonSyntaxException,
			IOException {
		return GSON.fromJson(getResponseText(), ofType);
	}

	/**
	 * Uses GSON to parse the response text as JSON encoding of a
	 * Map<String,Object>, that is, a dictionary of names and values;
	 * 
	 * @return
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getResponseJson() throws JsonSyntaxException,
			IOException {
		return getResponseJson(Map.class);
	}

	/**
	 * Uses the default SAX parser to read the response input stream as an XML
	 * document;
	 * 
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public Document getResponseXml() throws SAXException,
			IOException,
			ParserConfigurationException {
		return XML_BUILDER_FACTORY
				.newDocumentBuilder()
				.parse(getInputStream());
	}

}
