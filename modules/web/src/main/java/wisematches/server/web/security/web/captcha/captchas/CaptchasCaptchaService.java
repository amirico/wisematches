package wisematches.server.web.security.web.captcha.captchas;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.NativeWebRequest;
import wisematches.playground.GameMessageSource;
import wisematches.server.web.security.web.captcha.CaptchaService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;

/**
 * This is implementation of {@code CaptchaService} based on http://captchas.net.
 * <p/>
 * This code partly taken from here: http://captchas.net/sample/jsp/captchasdotnet
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CaptchasCaptchaService implements CaptchaService {
	private String publicKey;
	private String privateKey;

	private int width = 180;
	private int height = 80;
	private int lettersCount = 6;
	private String alphabet = "abcdefghkmnopqrstuvwxyz";

	private static final Random RANDOM = new Random();
	private static final ThreadLocal<MessageDigest> DIGEST = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException ex) {
				return null;
			}
		}
	};

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.security.CaptchasCaptchaService");

	public CaptchasCaptchaService() {
	}

	@Override
	public String createCaptchaScript(GameMessageSource messageSource, Locale locale) {
		final String random = randomString();

		final StringBuilder imageCode = new StringBuilder();
		imageCode.append("<div class=\"captchas\">");
		imageCode.append("<div class=\"image\"><img id=\"captchas.net\" src=\"").append(imageUrl(random)).append("\" ");
		imageCode.append("width=\"").append(width).append("\" height=\"").append(height).append("\" ");
		imageCode.append("alt=\"The CAPTCHA image\" /></div>");
		imageCode.append("<label for=\"captchaValue\">").append(messageSource.getMessage("captcha.type", locale)).append(": </label><br>");
		imageCode.append("<input id=\"captchaValue\" name=\"captchaValue\"></input>");
		imageCode.append("<input id=\"captchaRandom\" name=\"captchaRandom\" type=\"hidden\" value=\"").append(random).append("\"></input>");
		imageCode.append("</div>");
		return imageCode.toString();
	}

	@Override
	public void validateCaptcha(NativeWebRequest request, Errors errors) {
		final String captchaValue = request.getParameter("captchaValue");
		final String captchaRandom = request.getParameter("captchaRandom");
		if (captchaValue == null || captchaRandom == null) {
			errors.rejectValue("captcha", "captcha.err.verify-params-incorrect");
			return;
		}

		if (captchaValue.length() != lettersCount) {
			errors.rejectValue("captcha", "captcha.err.incorrect-captcha-sol");
			return;
		}

		final MessageDigest messageDigest = DIGEST.get();
		if (messageDigest == null) {
			log.error("No message digest for MD5");
			errors.rejectValue("captcha", "captcha.err.unknown");
			return;
		}

		final String encryptionBase = privateKey + captchaRandom + ":" + alphabet + ":" + lettersCount;
		final byte[] digest = messageDigest.digest(encryptionBase.getBytes());
		final char[] chars = captchaValue.toCharArray();


		for (int i = 0; i < lettersCount; i++) {
			int index = (digest[i] + 256) % 256 % alphabet.length();
			if (chars[i] != alphabet.charAt(index)) {
				errors.rejectValue("captcha", "captcha.err.incorrect-captcha-sol");
				return;
			}
		}
	}

	private String randomString() {
		return Integer.toHexString(RANDOM.nextInt()) + Integer.toHexString(RANDOM.nextInt());
	}

	private String imageUrl(String random) {
		String url = "http://image.captchas.net/";
		url += "?client=" + publicKey;
		url += "&width=" + width;
		url += "&height=" + height;
		url += "&random=" + random;
		url += "&alphabet=" + alphabet;
		url += "&letters=" + lettersCount;
		return url;
	}

	/**
	 * Generate audio url with parameters
	 * same as image url without width and height
	 */
	private String audioUrl(String random) {
		String url = "http://audio.captchas.net/";
		url += "?client=" + publicKey;
		url += "&random=" + random;
		url += "&alphabet=" + alphabet;
		url += "&letters=" + lettersCount;
		return url;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public void setLettersCount(int lettersCount) {
		this.lettersCount = lettersCount;
	}
}
