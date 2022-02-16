package com.social.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.social.entities.Follow;
import com.social.entities.FollowRequest;
import com.social.entities.User;
import com.social.helper.FollowUser;
import com.social.repositories.FollowRepository;
import com.social.repositories.FollowRequestRepository;

@Service
public class FollowService {

	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private FollowRequestRepository followRequestRepository;

	/*
	 * user follow profileUser
	 */
	public boolean ifFollowed(User user, User profileUser) {
		return this.followRepository.existsByFollowByUserAndFollowedToUser(user, profileUser);
	}
	
	/*
	 * user send request to profileUser
	 */
	public boolean isSendRequest(User user, User profileUser) {
		return this.followRequestRepository.existsByUserAndUserWhoSend(profileUser, user);
	}

	public long countUserFollowing(User user) {
		return this.followRepository.countByFollowByUser(user);
	}
	
	public long countUserFollowers(User user) {
		return this.followRepository.countByFollowedToUser(user);
	}

	public String doFollow(User user, User profileUser) {
		if(profileUser.getUserData().isAccountMode()) {
			if(!ifFollowed(user, profileUser)) {
				Follow follow = new Follow();
				follow.setFollowByUser(user);
				follow.setFollowedToUser(profileUser);
				this.followRepository.save(follow);
			}
			return "done";
		} else {
			if(!isSendRequest(user, profileUser)) {
				FollowRequest followRequest = new FollowRequest();
				followRequest.setUser(profileUser);
				followRequest.setUserWhoSend(user);
				this.followRequestRepository.save(followRequest);	
			}
			return "private";
		}
	}

	public String doUnfollow(User user, User profileUser) {
		if(ifFollowed(user, profileUser)) {
			Follow follow = this.followRepository.getByFollowByUserAndFollowedToUser(user, profileUser);
			this.followRepository.deleteById(follow.getId());
			return "done";
		}
		return "no";
	}

	public String removeFollower(User user, User profileUser) {
		if(ifFollowed(profileUser, user)) {
			Follow follow = this.followRepository.getByFollowByUserAndFollowedToUser(profileUser, user);
			this.followRepository.deleteById(follow.getId());
			return "done";
		}
		return "no";
	}
	
	public String cancelFollowRequest(User user, User profileUser) {
		if(isSendRequest(user, profileUser)) {
			FollowRequest followRequest = this.followRequestRepository.getByUserAndUserWhoSend(profileUser, user);
			this.followRequestRepository.deleteById(followRequest.getId());
			return "done";
		} else {
			return "no";
		}
	}

	public String acceptFollowRequest(User user, User profileUser) {
		if(isSendRequest(profileUser, user)) {
			Follow follow = new Follow();
			follow.setFollowByUser(profileUser);
			follow.setFollowedToUser(user);
			this.followRepository.save(follow);
			FollowRequest followRequest = this.followRequestRepository.getByUserAndUserWhoSend(user, profileUser);
			this.followRequestRepository.deleteById(followRequest.getId());
			return "done";
		} else {
			return "no";
		}
	}

	public String declineFollowRequest(User user, User profileUser) {
		if(isSendRequest(profileUser, user)) {
			FollowRequest followRequest = this.followRequestRepository.getByUserAndUserWhoSend(user, profileUser);
			this.followRequestRepository.deleteById(followRequest.getId());
			return "done";
		} else {
			return "no";
		}
	}

	public List<Follow> getFollowing(User user){
		List<Follow> list = this.followRepository.getByFollowByUser(user);
		 List<Follow> reverseList = new ArrayList<Follow>();
		 for(int i=list.size()-1;i>=0; i--) {
			reverseList.add(list.get(i)); 
		 }
		 return reverseList;
	}
	
	public List<Follow> getFollowers(User user){
		 List<Follow> list = this.followRepository.getByFollowedToUser(user);
		 List<Follow> reverseList = new ArrayList<Follow>();
		 for(int i=list.size()-1;i>=0; i--) {
			reverseList.add(list.get(i)); 
		 }
		 return reverseList;
	}

	public List<FollowRequest> getSentFollowRequests(User user) {
		List<FollowRequest> list = this.followRequestRepository.getByUserWhoSend(user);
		List<FollowRequest> reverseList = new ArrayList<FollowRequest>();
		 for(int i=list.size()-1;i>=0; i--) {
			reverseList.add(list.get(i)); 
		 }
		 return reverseList;
	}
	
	public List<FollowRequest> getReceivedFollowRequests(User user) {
		List<FollowRequest> list = this.followRequestRepository.getByUser(user);
		List<FollowRequest> reverseList = new ArrayList<FollowRequest>();
		 for(int i=list.size()-1;i>=0; i--) {
			reverseList.add(list.get(i)); 
		 }
		 return reverseList;
	}

	public List<FollowUser> getUserFollowing(User profileUser) {
		List<Follow> list = this.followRepository.getByFollowByUser(profileUser);
		List<FollowUser> result = new LinkedList<FollowUser>();
		for(int i=list.size()-1;i>=0; i--) {
			result.add(new FollowUser(list.get(i).getFollowedToUser().getFirstName()+" "+list.get(i).getFollowedToUser().getLastName(),
										list.get(i).getFollowedToUser().getUserData().getProfileImagePath(),
										list.get(i).getFollowedToUser().getUserName())); 
		 }
		return result;
	}

	public List<FollowUser> getUserFollowers(User profileUser) {
		List<Follow> list = this.followRepository.getByFollowedToUser(profileUser);
		List<FollowUser> result = new LinkedList<FollowUser>();
		for(int i=list.size()-1;i>=0; i--) {
			result.add(new FollowUser(list.get(i).getFollowByUser().getFirstName()+" "+list.get(i).getFollowByUser().getLastName(),
										list.get(i).getFollowByUser().getUserData().getProfileImagePath(),
										list.get(i).getFollowByUser().getUserName())); 
		 }
		return result;
	}
		
}
