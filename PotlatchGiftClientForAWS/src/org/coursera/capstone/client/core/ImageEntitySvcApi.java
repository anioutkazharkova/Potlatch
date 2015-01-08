package org.coursera.capstone.client.core;

import org.coursera.capstone.client.core.entities.ImageEntity;
import org.coursera.capstone.client.core.entities.ImageStatus;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

public interface ImageEntitySvcApi {

	public static final String IMAGE_SVC_PATH="/image";
	public static final String IMAGE_GIFT_ID_SEARCH_PATH = IMAGE_SVC_PATH + "/search/findByGiftId";
	public static final String IMAGE_DATA_PATH = IMAGE_SVC_PATH + "/{id}/data";
	public static final String IMAGE_URL_PATH="{url}";
	
	@POST(IMAGE_SVC_PATH)
	public ImageEntity addImage(@Body ImageEntity image);
	
	@GET(IMAGE_GIFT_ID_SEARCH_PATH)
	public ImageEntity findByGiftId(@Query("giftId") String giftId);
	
	@Multipart
	@POST(IMAGE_DATA_PATH)	
	public ImageStatus setImageData(@Path("id")String id, @Part("data") TypedFile data);
	
	@Streaming
    @GET(IMAGE_DATA_PATH)
    Response getData(@Path("id")String id);
}
