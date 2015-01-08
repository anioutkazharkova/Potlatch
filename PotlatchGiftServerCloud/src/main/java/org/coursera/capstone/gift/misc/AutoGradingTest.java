package org.coursera.capstone.gift.misc;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.catalina.connector.RequestFacade;
import org.apache.http.HttpStatus;
import org.coursera.capstone.gift.client.GiftEntitySvcApi;
import org.coursera.capstone.gift.client.ImageEntitySvcApi;
import org.coursera.capstone.gift.client.SecuredRestBuilder;
import org.coursera.capstone.gift.client.UserEntitySvcApi;
import org.coursera.capstone.gift.repository.GiftEntity;
import org.coursera.capstone.gift.repository.ImageEntity;
import org.coursera.capstone.gift.repository.UserEntity;
import org.junit.Test;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class AutoGradingTest {

	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}
	
	private File testData = new File(
			"src/test/java/test.jpg");

	private final String TEST_URL ="http://imageservice.elasticbeanstalk.com";// "https://localhost:8443";
 private final String TOKEN_PATH="/oauth/token";
	private final String USERNAME1 = "admin";
	private final String USERNAME2 = "user0";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";

	
	
	private UserEntitySvcApi userService = new SecuredRestBuilder()
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL)
	.setLoginEndpoint(TEST_URL + TOKEN_PATH)
	 .setLogLevel(LogLevel.FULL)
	.setUsername(USERNAME2).setPassword(PASSWORD).setClientId(CLIENT_ID)
	.build().create(UserEntitySvcApi.class);
	
	private GiftEntitySvcApi giftService = new SecuredRestBuilder()
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL)
	.setLoginEndpoint(TEST_URL +TOKEN_PATH)
	.setLogLevel(LogLevel.FULL)
	.setUsername(USERNAME2).setPassword(PASSWORD).setClientId(CLIENT_ID)
	.build().create(GiftEntitySvcApi.class);
	
	/*private ImageEntitySvcApi imageService = new SecuredRestBuilder()
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL)
	.setLoginEndpoint(TEST_URL + TOKEN_PATH)
	// .setLogLevel(LogLevel.FULL)
	.setUsername(USERNAME1).setPassword(PASSWORD).setClientId(CLIENT_ID)
	.build().create(ImageEntitySvcApi.class);
*/
	private ImageEntitySvcApi imageService = new RestAdapter.Builder().setRequestInterceptor(new RequestInterceptor() {
		
		@Override
		public void intercept(RequestFacade request) {
			// TODO Auto-generated method stub
			request.addHeader("Accept", "application/json");
		}
	})
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(ImageEntitySvcApi.class);


	


	
	/*@Test
	public void testAddVideoMetadata() throws Exception {
		Video received = readWriteVideoSvcUser1.addVideo(video);
		assertEquals(video.getName(), received.getName());
		assertEquals(video.getDuration(), received.getDuration());
		assertTrue(received.getLikes() == 0);
		assertTrue(received.getId() > 0);
	}*/

	@Test
	public void testAddUsersList() throws Exception
	{
	//	UserEntity user1=userService.addUser(new UserEntity(USERNAME1,PASSWORD));
	//	UserEntity user2= userService.addUser(new UserEntity(USERNAME2,PASSWORD));
		userService.getUsersList();
	}
	@Test
	public void testGetAuth() throws Exception
	{
		UserEntity user=userService.getAuthorities();
	}
	@Test
	public void testAddGift() throws Exception
	{
		GiftEntity gift=new GiftEntity();
		gift.setText("text");
		gift.setTitle("title");
		//gift.setChainId(1);
	GiftEntity receivedGift=	giftService.addGift(gift);
		/*CommentEntity commentEntity=new CommentEntity();
		commentEntity.setIsTouched(1);
		//giftService.addComment(commentEntity, receivedGift.getId());
		CommentEntity comment2=new CommentEntity();
		comment2.setIsObscene(1);
		comment2.setIsInappropriate(1);
		//comment2.setIsTouched(0);
		//giftService.addComment(comment2, receivedGift.getId());
	
		userService.addGiftToCount();*/
		//userService.
	}
	@Test
	public void testGetByChain()
	{
		
		//giftService.findByChainId(1);
	}
	@Test
	public void testAddToChain()
	{
		GiftEntity gift=new GiftEntity();
		gift.setText("text");
		gift.setTitle("title");
		gift.setDate(new Date().toString());
		//giftService.addGiftToChain(gift,1);
	}

	@Test
	public void testGetUsersGifts() throws Exception
	{
		giftService.findByUserToken();
	}
	@Test
	public void testAddImageToGift() throws Exception
	{
	/*	GiftEntity gift=new GiftEntity();
		gift.setText("text");
		gift.setTitle("title");
	GiftEntity gift2=	giftService.addGift(gift);
		userService.addGiftToCount();*/
		
		ImageEntity image=new ImageEntity();
		image.setName(testData.getName());
		image.setContentType("image/jpeg");
		image.setGiftId("1111");
		ImageEntity received=imageService.addImage(image);
		imageService.setImageData(received.getId(), new TypedFile(received.getContentType(), testData));
		
		Response response = imageService.getData(received.getId());
		assertEquals(200, response.getStatus());
		
		InputStream videoData = response.getBody().in();
		File file=new File("C:\\temp\\temp");
		OutputStream saved=new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
 
		while ((read = videoData.read(bytes)) != -1) {
			saved.write(bytes, 0, read);
		}
			videoData.close();
			saved.close();
		
	}
	
	@Test
	public void testDenyVideoAddWithoutOAuth() throws Exception {
		ErrorRecorder error = new ErrorRecorder();

		// Create an insecure version of our Rest Adapter that doesn't know how
		// to use OAuth.
		UserEntitySvcApi insecurevideoService = new RestAdapter.Builder()
				.setClient(
						new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
				.setErrorHandler(error).build().create(UserEntitySvcApi.class);
		try {
			// This should fail because we haven't logged in!
			//insecurevideoService.addVideo(video);
insecurevideoService.getAuthorities();
			//fail("Yikes, the security setup is horribly broken and didn't require the user to authenticate!!");

		} catch (Exception e) {
			// Ok, our security may have worked, ensure that
			// we got a 401
			assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
					.getResponse().getStatus());
		}
	}
/*	@Test
	public void testAddGetVideo() throws Exception {
		readWriteVideoSvcUser1.addVideo(video);
		Collection<Video> stored = readWriteVideoSvcUser1.getVideoList();
		assertTrue(stored.contains(video));
	}

	
	@Test
	public void testDenyVideoAddWithoutOAuth() throws Exception {
		ErrorRecorder error = new ErrorRecorder();

		// Create an insecure version of our Rest Adapter that doesn't know how
		// to use OAuth.
		VideoSvcApi insecurevideoService = new RestAdapter.Builder()
				.setClient(
						new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL)
				.setErrorHandler(error).build().create(VideoSvcApi.class);
		try {
			// This should fail because we haven't logged in!
			insecurevideoService.addVideo(video);

			fail("Yikes, the security setup is horribly broken and didn't require the user to authenticate!!");

		} catch (Exception e) {
			// Ok, our security may have worked, ensure that
			// we got a 401
			assertEquals(HttpStatus.SC_UNAUTHORIZED, error.getError()
					.getResponse().getStatus());
		}

		// We should NOT get back the video that we added above!
		Collection<Video> videos = readWriteVideoSvcUser1.getVideoList();
		assertFalse(videos.contains(video));
	}

	
	@Test
	public void testLikeCount() throws Exception {

		// Add the video
		Video v = readWriteVideoSvcUser1.addVideo(video);

		// Like the video
		readWriteVideoSvcUser1.likeVideo(v.getId());

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is 1
		assertTrue(v.getLikes() == 1);

		// Unlike the video
		readWriteVideoSvcUser1.unlikeVideo(v.getId());

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is 0
		assertTrue(v.getLikes() == 0);
	}

	
	@Test
	public void testLikedBy() throws Exception {

		// Add the video
		Video v = readWriteVideoSvcUser1.addVideo(video);

		// Like the video
		readWriteVideoSvcUser1.likeVideo(v.getId());

		Collection<String> likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());

		// Make sure we're on the list of people that like this video
		assertTrue(likedby.contains(USERNAME1));
		
		// Have the second user like the video
		readWriteVideoSvcUser2.likeVideo(v.getId());
		
		// Make sure both users show up in the like list
		likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());
		assertTrue(likedby.contains(USERNAME1));
		assertTrue(likedby.contains(USERNAME2));

		// Unlike the video
		readWriteVideoSvcUser1.unlikeVideo(v.getId());

		// Get the video again
		likedby = readWriteVideoSvcUser1.getUsersWhoLikedVideo(v.getId());

		// Make sure user1 is not on the list of people that liked this video
		assertTrue(!likedby.contains(USERNAME1));
		
		// Make sure that user 2 is still there
		assertTrue(likedby.contains(USERNAME2));
	}

	
	@Test
	public void testLikingTwice() throws Exception {

		// Add the video
		Video v = readWriteVideoSvcUser1.addVideo(video);

		// Like the video
		readWriteVideoSvcUser1.likeVideo(v.getId());

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is 1
		assertTrue(v.getLikes() == 1);

		try {
			// Like the video again.
			readWriteVideoSvcUser1.likeVideo(v.getId());

			fail("The server let us like a video twice without returning a 400");
		} catch (RetrofitError e) {
			// Make sure we got a 400 Bad Request
			assertEquals(400, e.getResponse().getStatus());
		}

		// Get the video again
		v = readWriteVideoSvcUser1.getVideoById(v.getId());

		// Make sure the like count is still 1
		assertTrue(v.getLikes() == 1);
	}

	
	@Test
	public void testLikingNonExistantVideo() throws Exception {

		try {
			// Like the video again.
			readWriteVideoSvcUser1.likeVideo(getInvalidVideoId());

			fail("The server let us like a video that doesn't exist without returning a 404.");
		} catch (RetrofitError e) {
			// Make sure we got a 400 Bad Request
			assertEquals(404, e.getResponse().getStatus());
		}
	}

	
	@Test
	public void testFindByName() {

		// Create the names unique for testing.
		String[] names = new String[3];
		names[0] = "The Cat";
		names[1] = "The Spoon";
		names[2] = "The Plate";

		// Create three random videos, but use the unique names
		ArrayList<Video> videos = new ArrayList<Video>();

		for (int i = 0; i < names.length; ++i) {
			videos.add(TestData.randomVideo());
			videos.get(i).setName(names[i]);
		}

		// Add all the videos to the server
		for (Video v : videos){
			readWriteVideoSvcUser1.addVideo(v);
		}

		// Search for "The Cat"
		Collection<Video> searchResults = readWriteVideoSvcUser1.findByTitle(names[0]);
		assertTrue(searchResults.size() > 0);

		// Make sure all the returned videos have "The Cat" for their title
		for (Video v : searchResults) {
			assertTrue(v.getName().equals(names[0]));
		}
	}
*/
	/**
	 * Test finding a video by its duration.
	 */
	/*
	@Test
	public void testFindByDurationLessThan() {

		// Create the durations unique for testing.
		long[] durations = new long[3];
		durations[0] = 1;
		durations[1] = 5;
		durations[2] = 9;

		// Create three random videos, but use the unique durations
		ArrayList<Video> videos = new ArrayList<Video>();

		for (int i = 0; i < durations.length; ++i) {
			videos.add(TestData.randomVideo());
			videos.get(i).setDuration(durations[0]);
		}

		// Add all the videos to the server
		for (Video v : videos){
			readWriteVideoSvcUser1.addVideo(v);
		}

		// Search for "The Cat"
		Collection<Video> searchResults = readWriteVideoSvcUser1.findByDurationLessThan(6L);
		// Make sure that we have at least two videos
		assertTrue(searchResults.size() > 1);

		for (Video v : searchResults) {
			// Make sure that all of the videos are of the right duration
			assertTrue(v.getDuration() < 6);
		}
	}

	private long getInvalidVideoId() {
		Set<Long> ids = new HashSet<Long>();
		Collection<Video> stored = readWriteVideoSvcUser1.getVideoList();
		for (Video v : stored) {
			ids.add(v.getId());
		}

		long nonExistantId = Long.MIN_VALUE;
		while (ids.contains(nonExistantId)) {
			nonExistantId++;
		}
		return nonExistantId;
	}*/

}
