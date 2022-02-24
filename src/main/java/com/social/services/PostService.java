package com.social.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.social.entities.Follow;
import com.social.entities.Notification;
import com.social.entities.Post;
import com.social.entities.User;
import com.social.helper.AnimatedGifEncoder;
import com.social.helper.GifDecoder;
import com.social.helper.HomePost;
import com.social.repositories.FollowRepository;
import com.social.repositories.LikeRepository;
import com.social.repositories.NotificationRepository;
import com.social.repositories.PostRepository;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoSize;
 
@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private FollowRepository followRepository;

	public List<HomePost> getHomePost(User user, int pageNo) {
		List<Follow> following = this.followRepository.getByFollowByUser(user);
		List<Integer> posts = new LinkedList<Integer>();
		following.forEach(f->{
			User u = f.getFollowedToUser();
			List<Post> byUser = this.postRepository.getByUser(u);
			byUser.forEach(p->{
				posts.add(p.getId());
			});
		});
		Collections.sort(posts);
		List<HomePost> list = new LinkedList<HomePost>();
		for(int i = posts.size()-1-(10*(pageNo-1)), k=1; i>=0 && k<=10; i--, k++) {
			Post post = this.getPostById(posts.get(i));
			User user2 = post.getUser();
			HomePost homePost = new HomePost();
			homePost.setCaption(post.getCaption());
			homePost.setEdit(post.isEdit());
			homePost.setFormat(post.getFormat());
			homePost.setPathOfPost(post.getPathOfPost());;
			homePost.setId(post.getId());
			homePost.setName(user2.getFirstName()+" "+user2.getLastName());
			homePost.setProfile(user2.getUserData().getProfileImagePath());
			homePost.setTotalLikes(this.likeRepository.countByPost(post));
			homePost.setUsername(user2.getUserName());
			list.add(homePost);
		}
		return list;
	}

	public long countUserPost(User user) {
		return this.postRepository.countByUser(user);
	}

	public Post getPostById(int id) {
		return this.postRepository.getById(id);
	}
	
	public boolean checkPostExistById(int id) {
		return this.postRepository.existsById(id);
	}

	public List<Post> getPostByUser(User user) {
		List<Post> list = this.postRepository.getByUser(user);
		List<Post> reverseList = new LinkedList<Post>();
		for(int i = list.size()-1; i>=0; i--) {
			reverseList.add(list.get(i));
		}
		return reverseList;
	}
	
	public Post getPostByIdAndUser(int id, User userByEmail) {
		return this.postRepository.getByIdAndUser(id, userByEmail);
	}
	
	public Post getPostByPostPath(String pathOfPost) {
		return this.postRepository.getByPathOfPost(pathOfPost);
	}
	
	public String uploadPost(User user, String caption, MultipartFile file) {
		try {
			String contentType = file.getContentType();
			InputStream inputStream = file.getInputStream();
			String postType = null;
			Date date = new Date();
			String postPath = null;
			boolean flag = false;
			switch (contentType) {
			case "image/jpeg":
	        	BufferedImage image = ImageIO.read(inputStream);
				BufferedImage resizeImage = this.simpleResizeImage(image, 480);
				postPath = user.getId()+"_"+date.getTime()+".jpg";
				postType = "image";
				flag = this.savePost(resizeImage, postPath, "jpeg");
				break;
			case "image/png":
	        	BufferedImage image2 = ImageIO.read(inputStream);
				BufferedImage resizeImage2 = this.simpleResizeImage(image2, 480);
				postPath = user.getId()+"_"+date.getTime()+".png";
				postType = "image";
				flag = this.savePost(resizeImage2, postPath, "png");
				break;
			case "image/gif":
				InputStream inputStream2 = this.simpleResizeGif(inputStream, 480);
				postPath = user.getId()+"_"+date.getTime()+".gif";
				postType = "image";
				flag = this.saveInDisk(inputStream2, postPath);
				break;
			case "video/mp4":
				postPath = user.getId()+"_"+date.getTime()+".mp4";
				String postPathTemp = user.getId()+"_"+date.getTime()+"_temp.mp4";
				postType = "video";
				flag = this.simpleVideoResize(inputStream, postPathTemp, postPath);
				break;
			case "video/x-ms-wmv":
				postPath = user.getId()+"_"+date.getTime()+".mp4";
				String postPathTemp2 = user.getId()+"_"+date.getTime()+"_temp.wmv";
				postType = "video";
				flag = this.simpleVideoResize(inputStream, postPathTemp2, postPath);
				break;
			case "video/avi":
				postPath = user.getId()+"_"+date.getTime()+".mp4";
				String postPathTemp3 = user.getId()+"_"+date.getTime()+"_temp.avi";
				postType = "video";
				flag = this.simpleVideoResize(inputStream, postPathTemp3, postPath);
				break;
			default:
				return "invalidFileType";
			}
			if(flag) {
				Post post = new Post();
				post.setCaption(caption);
				post.setDate(new Date());
				post.setEdit(false);
				post.setFormat(postType);
				post.setPathOfPost(postPath);
				post.setUser(user);
				this.postRepository.save(post);
				Notification notification = new Notification();
				notification.setDate(date);
				notification.setIconPath(user.getUserData().getProfileImagePath());
				notification.setRedirectUrl(post.getId()+"");
				notification.setSeen(false);
				notification.setText("Post Successfully Uploaded");
				notification.setUser(user);
				this.notificationRepository.save(notification);
				return "done";
			} else {
				return "fileError";
			}
		} catch (Exception e) {
			return "no";
		}
	}
	
	public String editPost(User user, String caption, int postId) {
		Post post = this.postRepository.getByIdAndUser(postId, user);
		if(post==null) {
			return "notFound";
		}
		post.setCaption(caption);
		post.setEdit(true);
		this.postRepository.save(post);
		return "done";
	}
	
	public String deletePost(User user, int postId) {
		Post post = this.postRepository.getByIdAndUser(postId, user);
		if(post==null) {
			return "no";
		}
		try {
			Files.delete(Paths.get(new ClassPathResource("static").getFile().getAbsolutePath()+
					java.io.File.separator+"img"+
					java.io.File.separator+"post"+
					java.io.File.separator+post.getPathOfPost()));
		} catch (IOException e) {
			return "no";
		}
		this.postRepository.deleteById(post.getId());
		return "done";
	}
	
	private boolean simpleVideoResize(InputStream inputStream, String postPathTemp, String postPath) throws IOException, InputFormatException, EncoderException {
		boolean flag = false;
		boolean check = this.saveInDisk(inputStream, postPathTemp);
		String staticPath = new ClassPathResource("static").getFile().getAbsolutePath()+
							File.separator+"img"+File.separator+"post"+File.separator;
		if(check) {
			File file = new File(staticPath+postPathTemp);
			File outputFile = new File(staticPath+postPath);
			flag = this.videoConvert(file, outputFile, 480);
			if(!flag && outputFile.exists()) {
				outputFile.delete();
			}
			if(file.exists()) {
				file.delete();
			}
		} else {
			return false;
		}
		return true;
	}
	
	private boolean videoConvert(File file, File outputFile, int targetWidth) throws InputFormatException, EncoderException{
		MultimediaObject multimediaObject = new MultimediaObject(file);
		int bitRate2 = 64000;
		int channels = 2;
		int samplingRate = 48000;
		if(multimediaObject.getInfo().getAudio()!=null) {
			bitRate2 = multimediaObject.getInfo().getAudio().getBitRate();
			channels = multimediaObject.getInfo().getAudio().getChannels();
			samplingRate = multimediaObject.getInfo().getAudio().getSamplingRate();
		}
		float frameRate = multimediaObject.getInfo().getVideo().getFrameRate();
		if(frameRate>15) {
			frameRate = 15;
		}
		int bitRate = 2048000;
		VideoSize size = multimediaObject.getInfo().getVideo().getSize();
		int width = size.getWidth();
		int height = size.getHeight();
		double ratio = (double)width/targetWidth;
		VideoSize outputSize = new VideoSize(targetWidth, (int)(height/ratio));
		
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        audio.setBitRate(bitRate2);
        audio.setChannels(channels);
        audio.setSamplingRate(samplingRate);
        
        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
        video.setBitRate(bitRate);
        video.setFrameRate((int)frameRate);
        video.setSize(outputSize);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        
        try {
            Encoder encoder = new Encoder();
			encoder.encode(multimediaObject, outputFile, attrs);
			return true;
        } catch (IllegalArgumentException | EncoderException e) {
        	return false;
        }
    }
	
	private BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, Scalr.OP_ANTIALIAS); 
    }

	private InputStream simpleResizeGif(InputStream io, int targetWidth) throws Exception {
		ByteArrayOutputStream iOutputStream = new ByteArrayOutputStream();
		GifDecoder gifDecoder = new GifDecoder();
		gifDecoder.read(io);
		int n = gifDecoder.getFrameCount();
		int delay = gifDecoder.getDelay(0);
		AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
		animatedGifEncoder.start(iOutputStream);
		animatedGifEncoder.setDelay(delay);
		animatedGifEncoder.setRepeat(0);
		for (int i = 0; i < n; i++) {
	       BufferedImage frame = gifDecoder.getFrame(i);
	       animatedGifEncoder.addFrame(this.simpleResizeImage(frame, targetWidth));
	    }
	    animatedGifEncoder.finish();
	    byte[] bytes = iOutputStream.toByteArray();
	    InputStream inputStream = new ByteArrayInputStream(bytes);
		return inputStream;
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
						java.io.File.separator+"post"+
						java.io.File.separator+path),
				StandardCopyOption.REPLACE_EXISTING);
		return true;
    }

}
