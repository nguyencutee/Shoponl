package com.ecom.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import com.ecom.service.impl.UserServiceImpl;
import com.ecom.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");

		UserDtls userDtls = userRepository.findByEmail(email);

		if (userDtls != null) {

			if (userDtls.getIsEnable()) {

				if (userDtls.getAccountNonLocked()) {

					if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
						userService.increaseFailedAttempt(userDtls);
					} else {
						userService.userAccountLock(userDtls);
						exception = new LockedException("Tài khoản của bạn đã bị khóa! Lần thử 3 thất bại");
					}
				} else {

					if (userService.unlockAccountTimeExpired(userDtls)) {
						exception = new LockedException("Tài khoản của bạn đã được mở khóa! Hãy thử đăng nhập");
					} else {
						exception = new LockedException("Tài khoản của bạn đã bị khóa! Hãy thử lại vào lúc khác");
					}
				}

			} else {
				exception = new LockedException("Tài khoản của bạn đã bị khoá");
			}
		} else {
			exception = new LockedException("Email và mật khẩu không hợp lệ");
		}

		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}
