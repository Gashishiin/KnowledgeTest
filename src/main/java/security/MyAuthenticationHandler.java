package security;

import base.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class MyAuthenticationHandler implements AuthenticationSuccessHandler {
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority auth :
                authorities) {
            UserRole role = UserRole.valueOf(auth.toString());
            String url;
            switch (role){
                case ROLE_ADMIN:
                    url="users";
                    break;
                case ROLE_STUDENT:
                    url="test";
                    break;
                case ROLE_METHODIST:
                    url="questions";
                    break;
                    default:url="/";
            }
            httpServletResponse.sendRedirect(httpServletResponse.encodeURL(url));
        }
    }
}
