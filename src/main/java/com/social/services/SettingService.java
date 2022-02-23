package com.social.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.social.entities.Follow;
import com.social.entities.FollowRequest;
import com.social.entities.Post;
import com.social.entities.User;
import com.social.entities.UserData;
import com.social.repositories.FollowRepository;
import com.social.repositories.FollowRequestRepository;
import com.social.repositories.PostRepository;
import com.social.repositories.UserDataRepository;
import com.social.repositories.UserRepository;

@Service
public class SettingService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDataRepository userDataRepository;
	
	@Autowired
	private FollowRequestRepository followRequestRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	public String updatePassword(User user, String oldPassword, String newPassword) {
		if(this.passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
			return "done";
		} else {
			return "invalid";
		}
	}

	public String updateProfile(User user, String firstName, String lastName, String userName, String phone, String bio,
			String gender, Date dob) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(userName);
		UserData userData = user.getUserData();
		userData.setBio(bio);
		userData.setDateOfBirth(dob);
		userData.setGender(gender);
		userData.setPhone(phone);
		this.userRepository.save(user);
		this.userDataRepository.save(userData);
		return "done";
	}

	public String updateVisibility(User user, boolean accountMode, boolean showEmail, boolean showPhone,
			boolean showDOB) {
		if(accountMode) {
			List<FollowRequest> byUser = this.followRequestRepository.getByUser(user);
			byUser.forEach(fr->{
				Follow follow = new Follow();
				follow.setFollowByUser(fr.getUserWhoSend());
				follow.setFollowedToUser(fr.getUser());
				this.followRepository.save(follow);
				this.followRequestRepository.deleteById(fr.getId());
			});
		}
		UserData userData = user.getUserData();
		userData.setAccountMode(accountMode);
		userData.setShowDateOfBirth(showDOB);
		userData.setShowEmail(showEmail);
		userData.setShowPhone(showPhone);
		this.userDataRepository.save(userData);
		return "done";
	}
	
	public String updateProfilePic(User user, MultipartFile file) {
		String postPath = null;
		boolean flag = false;
		try {
			InputStream inputStream = file.getInputStream();
			String contentType = file.getContentType();
			switch(contentType) {
				case "image/jpeg":
					BufferedImage image = ImageIO.read(inputStream);
					BufferedImage resizeImage = this.simpleResizeAndCropImage(image, 300);
					postPath = user.getId()+".jpg";
					flag = this.savePost(resizeImage, postPath, "jpeg");
					// for 50x50
					BufferedImage resizeImage2 = this.simpleResizeImage(resizeImage, 50);
					this.savePost(resizeImage2, "50x50_"+postPath, "jpeg");
					break;
				case "image/png":
					BufferedImage image2 = ImageIO.read(inputStream);
					BufferedImage resizeImage3 = this.simpleResizeAndCropImage(image2, 300);
					postPath = user.getId()+".png";
					flag = this.savePost(resizeImage3, postPath, "png");
					// for 50x50
					BufferedImage resizeImage4 = this.simpleResizeImage(resizeImage3, 50);
					this.savePost(resizeImage4, "50x50_"+postPath, "png");
					break;
				default:
					return "invalidFileType";
			}
			if(flag) {
				UserData userData = user.getUserData();
				userData.setProfileImagePath(postPath);
				this.userDataRepository.save(userData);
				return "done";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "no";
	}

	private BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
		return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, Scalr.OP_ANTIALIAS);
	}
	
	private BufferedImage simpleResizeAndCropImage(BufferedImage originalImage, int targetWidth) throws Exception {
		int imageHeight = originalImage.getHeight();
		int imageWidth = originalImage.getWidth();
		BufferedImage resize;
		if(imageHeight>=imageWidth) {
			int tempTargetWidth = 300;
			double ratioDouble = (double)imageWidth/tempTargetWidth;
			int	tempTargetHeight = (int)(imageHeight/ratioDouble);
	        resize = Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, tempTargetWidth, tempTargetHeight, Scalr.OP_ANTIALIAS);			
		} else {
			int targetHeight = 300;
			double ratioDouble = (double)imageHeight/targetHeight;
			int	tempTargetWidth = (int)(imageWidth/ratioDouble);
	        resize = Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, tempTargetWidth, targetHeight, Scalr.OP_ANTIALIAS);
		}
        int height = resize.getHeight();
        int width = resize.getWidth();
        int x = 0, y = 0;
        if(height<width) {
        	x=(width-height)/2;
        } else if(height>width){
        	y=(height-width)/2;
        }
        BufferedImage crop = Scalr.crop(resize, x, y, targetWidth, targetWidth);
        return crop;
    }

	private boolean savePost(BufferedImage image, String path, String extension) throws IOException {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, extension, os); 
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			boolean b = this.saveInDisk(is, path);
			return b;
	}
	
	private boolean saveInDisk(InputStream inputStream, String path) throws IOException {
    	Files.copy(inputStream,
				Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
						java.io.File.separator+"img"+
						java.io.File.separator+"profile"+
						java.io.File.separator+path),
				StandardCopyOption.REPLACE_EXISTING);
		return true;
    }

	public String deleteAccount(User user) {
		List<Post> posts = user.getPosts();
		posts.forEach(post->{
			try {
				Files.delete(Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
						java.io.File.separator+"img"+
						java.io.File.separator+"post"+
						java.io.File.separator+post.getPathOfPost()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.postRepository.deleteById(post.getId());
		});
		String profileImagePath = user.getUserData().getProfileImagePath();
		if(!profileImagePath.equals("default.jpg")) {
			try {
				Files.delete(Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
						java.io.File.separator+"img"+
						java.io.File.separator+"profile"+
						java.io.File.separator+profileImagePath));
				Files.delete(Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
						java.io.File.separator+"img"+
						java.io.File.separator+"profile"+
						java.io.File.separator+"50x50_"+profileImagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.userRepository.deleteById(user.getId());
		return "done";
	}
	
}
