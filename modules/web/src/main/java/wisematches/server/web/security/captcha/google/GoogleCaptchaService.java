package wisematches.server.web.security.captcha.google;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import wisematches.server.web.i18n.GameMessageSource;
import wisematches.server.web.security.captcha.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GoogleCaptchaService implements CaptchaService {
	private String publicKey;
	private String privateKey;

	private int readTimeout = 1000;
	private int connectionTimeout = 1000;

	private String generationUrl = "http://www.google.com/recaptcha/api/challenge?k=";
	private String verificationUrl = "http://www.google.com/recaptcha/api/verify?";

	private static final Log log = LogFactory.getLog("wisematches.server.web.captcha");

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public GoogleCaptchaService() {
	}

	@Override
	public String createCaptchaScript(GameMessageSource messageSource, Locale language) {
		final StringBuilder b = new StringBuilder();
		b.append("<script type=\"text/javascript\">");
		b.append(LINE_SEPARATOR);
		b.append("var RecaptchaOptions = {");
		b.append(LINE_SEPARATOR);
		b.append("  theme: 'white',");
		b.append(LINE_SEPARATOR);
		b.append("  lang: '").append(language).append("'");
		b.append(LINE_SEPARATOR);
		b.append("};");
		b.append(LINE_SEPARATOR);
		b.append("</script>");
		b.append(LINE_SEPARATOR);
		b.append("<script type=\"text/javascript\" src=\"");
		b.append(generationUrl);
		b.append(publicKey);
		b.append("\"></script>");
		b.append("<input id=\"captcha\" name=\"captcha\" type=\"hidden\" value=\"true\"></input>");
		b.append(LINE_SEPARATOR);
		return b.toString();
	}

	@Override
	public void validateCaptcha(HttpServletRequest request, HttpServletResponse response, Errors errors) {
		final String removeAddress = request.getRemoteAddr();
		final String recaptcha_challenge_field = request.getParameter("recaptcha_challenge_field");
		final String recaptcha_response_field = request.getParameter("recaptcha_response_field");

		OutputStream out = null;
		BufferedReader in = null;

		try {
			final StringBuilder postData = new StringBuilder();
			postData.append("privatekey=").append(URLEncoder.encode(privateKey, "UTF-8"));
			postData.append("&remoteip=").append(URLEncoder.encode(removeAddress, "UTF-8"));
			postData.append("&challenge=").append(URLEncoder.encode(recaptcha_challenge_field, "UTF-8"));
			postData.append("&response=").append(URLEncoder.encode(recaptcha_response_field, "UTF-8"));

			final URLConnection connection = new URL(verificationUrl).openConnection();
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);

			connection.setDoOutput(true);
			connection.setDoInput(true);

			out = connection.getOutputStream();
			out.write(postData.toString().getBytes());
			out.flush();

			in = new LineNumberReader(new InputStreamReader(connection.getInputStream()));
			final String status = in.readLine(); // we can ignore this
			final String errorCode = in.readLine();

			if (status == null) {
				log.error("Very strange answer from server: not status");
				errors.rejectValue("captcha", "captcha.err.unknown");
			} else {
				if (!Boolean.parseBoolean(status) && errorCode != null) {
					errors.rejectValue("captcha", "captcha.err." + errorCode);
				}
			}
		} catch (SocketTimeoutException ex) {
			log.error("Google captcha system error", ex);
			errors.rejectValue("captcha", "captcha.err.recaptcha-not-reachable");
		} catch (IOException ex) {
			log.error("Google captcha system error", ex);
			errors.rejectValue("captcha", "captcha.err.unknown");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
	}


	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setGenerationUrl(String generationUrl) {
		this.generationUrl = generationUrl;
	}

	public void setVerificationUrl(String verificationUrl) {
		this.verificationUrl = verificationUrl;
	}
}
