package org.coursera.capstone.gift.client;

import java.util.Collection;

import org.coursera.capstone.gift.repository.CommentEntity;
import org.coursera.capstone.gift.repository.GiftEntity;
import org.springframework.http.converter.GenericHttpMessageConverter;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GiftEntitySvcApi {

public static final String TITLE_PARAMETER = "title";
public static final String CHAIN_ID="chainId";

	
	public static final String USER_TOKEN_PARAMETER = "userToken";

	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String GIFT_SVC_PATH = "/gift";
	
	public static final String GIFT_TITLE_SEARCH_PATH = GIFT_SVC_PATH + "/search/findByTitle";	
	
	public static final String GIFT_USER_TOKEN_SEARCH_PATH = GIFT_SVC_PATH + "/search/findByUserToken";
	
	public static final String GIFT_CHAINID_SEARCH_PATH = GIFT_SVC_PATH + "/search/findByChainId";
	
	public static final String GIFT_COMMENT_PATH=GIFT_SVC_PATH+"/{id}/comment";
	
	public static final String CHAIN_SVC_PATH=GIFT_SVC_PATH+"/{id}/chain";
	
	public static final String GIFT_FILTERED_PATH=GIFT_SVC_PATH+"/search/censored";

	@GET(GIFT_SVC_PATH)
	public Collection<GiftEntity> getGiftsList();
	@GET(GIFT_SVC_PATH+"/page/{page}")
	public Collection<GiftEntity> getGiftsPaged(@Path("page") long page);
	@GET(GIFT_SVC_PATH + "/{id}")
	public GiftEntity getGiftById(@Path("id") String id);
	
	@DELETE(GIFT_SVC_PATH+"/{id}")
	public GiftEntity removeGift(@Path("id") String id);
	
	@POST(GIFT_SVC_PATH)
	public GiftEntity addGift(@Body GiftEntity gift);
	
	@POST(CHAIN_SVC_PATH)
	public GiftEntity addGiftToChain(@Body GiftEntity gift,@Path("id") String id);
	
	@GET(GIFT_TITLE_SEARCH_PATH)
	public Collection<GiftEntity> findByTitle(@Query(TITLE_PARAMETER) String title);
	
	@GET(GIFT_TITLE_SEARCH_PATH+"/page/{page}")
	public Collection<GiftEntity> findByTitlePaged(@Query(TITLE_PARAMETER) String title,@Path("page") long page);
	
	@GET(GIFT_CHAINID_SEARCH_PATH)
	public Collection<GiftEntity> findByChainId(@Query(CHAIN_ID) String chainId);
	
	@GET(GIFT_CHAINID_SEARCH_PATH+"/page/{page}")
	public Collection<GiftEntity> findByChainIdPaged(@Query(CHAIN_ID) String chainId,@Path("page") long page);
	
	@GET(GIFT_USER_TOKEN_SEARCH_PATH)
	public Collection<GiftEntity> findByUserToken();
	
	@GET(GIFT_USER_TOKEN_SEARCH_PATH+"/page/{page}")
	public Collection<GiftEntity> findByUserTokenPage(@Path("page")long page);
	
	@POST(GIFT_COMMENT_PATH)
	public GiftEntity addComment(@Body CommentEntity comment,@Path("id") String id);
	
	@GET(GIFT_FILTERED_PATH)
	public Collection<GiftEntity> getGiftsFiltered();
	@GET(GIFT_FILTERED_PATH+"/page/{page}")
	public Collection<GiftEntity> getGiftsFilteredPaged(@Path("page") long page);
	
}
