package org.coursera.capstone.gift.repository;

import java.util.Collection;

import org.coursera.capstone.gift.client.GiftEntitySvcApi;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@EnableScan 
@RepositoryRestResource(path=GiftEntitySvcApi.GIFT_COMMENT_PATH)
public interface CommentEntityRepository extends
		CrudRepository<CommentEntity, String> {

	public Collection<CommentEntity> findByUserToken(String userToken);
	public Collection<CommentEntity> findByGiftId(String giftId);
	public Collection<CommentEntity> findByGiftIdAndUserToken(String giftId,String userToken);
	public Collection<CommentEntity> findByIsInappropriateOrIsObsceneAndUserToken(int isInappropriate, int isObscene,String userToken);
}
