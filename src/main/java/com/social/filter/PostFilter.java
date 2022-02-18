package com.social.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.social.config.UserDetailsImpl;
import com.social.entities.Post;
import com.social.entities.User;
import com.social.services.FollowService;
import com.social.services.PostService;
import com.social.services.ProfileService;

@Component
public class PostFilter implements Filter{
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FollowService followService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if(!httpServletRequest.getRequestURI().startsWith("/img/post/")) {
			chain.doFilter(httpServletRequest, response);
		} else {
			String requestURI = httpServletRequest.getRequestURI().substring(10);
			Post post = this.postService.getPostByPostPath(requestURI);
			User profileUser = post.getUser();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication!=null) {
				try {
					UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
					String username = userDetails.getUsername();
					User user = this.profileService.getUserByEmail(username);
					if(this.followService.ifFollowed(user, profileUser) || profileUser.getUserData().isAccountMode() || profileUser.getId() == user.getId()) {
						chain.doFilter(httpServletRequest, response);
					} else {
						HttpServletResponse httpServletResponse = (HttpServletResponse) response;
						httpServletResponse.sendError(404);
					}
				} catch (Exception e) {
					if(profileUser.getUserData().isAccountMode()) {
						chain.doFilter(httpServletRequest, response);
					} else {
						HttpServletResponse httpServletResponse = (HttpServletResponse) response;
						httpServletResponse.sendError(404);
					}
				}
			} else {
				if(profileUser.getUserData().isAccountMode()) {
					chain.doFilter(httpServletRequest, response);
				} else {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.sendError(404);
				}
			}
		}
	}	
	
}
