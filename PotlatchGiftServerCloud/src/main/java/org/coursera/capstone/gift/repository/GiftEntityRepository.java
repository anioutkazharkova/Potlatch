package org.coursera.capstone.gift.repository;

import java.util.Collection;

import org.coursera.capstone.gift.client.GiftEntitySvcApi;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@EnableScan 
@EnableScanCount
@RepositoryRestResource(path=GiftEntitySvcApi.GIFT_SVC_PATH)
public interface GiftEntityRepository extends PagingAndSortingRepository<GiftEntity,String> {

	
	public Collection<GiftEntity> findByTitle(String title);
	public Collection<GiftEntity> findByTitleContaining(String title);
	public Page<GiftEntity> findByTitleContaining(String title,Pageable pageable);
	public Collection<GiftEntity> findByChainId(String chainId);
	public Page<GiftEntity> findByChainId(String chainId,Pageable pageable);
	public Collection<GiftEntity> findByUserToken(String userToken);
	public Page<GiftEntity> findByUserToken(String userToken,Pageable pageable);
	
	public Collection<GiftEntity> findTop10ByOrderByIdDesc();
	//public Collection<GiftEntity> findAllByOrderByIdDesc();
}
