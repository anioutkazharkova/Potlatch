package org.coursera.capstone.gift.repository;

import java.util.Collection;

import org.coursera.capstone.gift.client.ImageEntitySvcApi;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@EnableScan 
@RepositoryRestResource(path=ImageEntitySvcApi.IMAGE_SVC_PATH)
public interface ImageEntityRepository extends CrudRepository<ImageEntity, String>{

public Collection<ImageEntity> findByName(String name);	
public ImageEntity findByUrl(String url);
public ImageEntity findByGiftId(String giftId);

}
