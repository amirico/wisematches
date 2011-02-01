package wisematches.server.deprecated.web.modules.core.problems;

import javax.persistence.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "rp_problems")
public class ProblemsReport {
	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(length = 100, nullable = false)
	private String username;

	@Column(length = 100, nullable = false)
	private String email;

	@Column(length = 100, nullable = false)
	private String account;

	private OperationSystems os;

	private Browser browser;

	@Column(length = 255, nullable = false)
	private String subject;

	@Column(nullable = false)
	private String message;

	public ProblemsReport() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public OperationSystems getOs() {
		return os;
	}

	public void setOs(OperationSystems os) {
		this.os = os;
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
