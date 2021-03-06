package net.cec.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.googlecode.objectify.Key;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

import net.cec.entities.Account;
import net.cec.utils.Secret;

import static com.google.common.base.Preconditions.checkArgument;
import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.*;
@WebFilter(urlPatterns = {"/*"})
public class TokenFilter implements Filter {
	
	static Logger logger = Logger.getLogger(TokenFilter.class.getName());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		
		String cookieToken = null;
		Cookie cookies[] = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie: cookies) {
			    if (cookie.getName().equals(Secret.TokenCookieName)) {
			        //do something
			        //value can be retrieved using #cookie.getValue()
			    	cookieToken = cookie.getValue();
			    }
			}
		}
		
		if(cookieToken!=null)
		{
			
			try {
				DefaultFacebookClient client = new DefaultFacebookClient(cookieToken,Version.LATEST);
				User user = client.fetchObject("/me", User.class, Parameter.with("fields",
					"email,name"));
				 request.setAttribute("User",user);
				 
				 logger.warning("User :"+user.getEmail() +" found!!");
			} catch (FacebookOAuthException FB) {
				 logger.warning(FB.getMessage());
				 
			}
			
			 
			 
		}
		
		
		chain.doFilter(request, response);
		
		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}