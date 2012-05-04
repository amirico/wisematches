package wisematches.server.web.security.spring;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AuthenticationFailure implements Serializable {
	private boolean rememberMe;

	public static final String NAME = "AuthenticationFailure";

	public AuthenticationFailure() {
/*
		<beans:entry
          key="org.springframework.security.authentication.BadCredentialsException"
          value="/account/loginAuth?error=credential"/>
  <beans:entry
          key="org.springframework.security.core.userdetails.UsernameNotFoundException"
          value="/account/loginAuth?error=credential"/>

  <beans:entry
          key="org.springframework.security.web.authentication.session.SessionAuthenticationException"
          value="/account/loginAuth?error=session"/>
  <beans:entry
          key="org.springframework.security.web.authentication.www.NonceExpiredException"
          value="/account/loginAuth?error=session"/>
  <beans:entry
          key="org.springframework.security.authentication.AuthenticationCredentialsNotFoundException"
          value="/account/loginAuth?error=session"/>
  <beans:entry
          key="org.springframework.security.authentication.InsufficientAuthenticationException"
          value="/account/loginAuth?error=session"/>

  <beans:entry
          key="org.springframework.security.authentication.AccountStatusException"
          value="/account/loginAuth?error=status"/>

  <beans:entry
          key="org.springframework.security.authentication.InsufficientAuthenticationException"
          value="/account/loginAuth?error=insufficient"/>
  <beans:entry
          key="org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException"
          value="/account/loginAuth?error=insufficient"/>
  <beans:entry
          key="org.springframework.security.web.authentication.rememberme.CookieTheftException"
          value="/account/loginAuth?error=insufficient"/>
  <beans:entry
          key="org.springframework.security.web.authentication.rememberme.InvalidCookieException"
          value="/account/loginAuth?error=insufficient"/>
*/
	}
}
