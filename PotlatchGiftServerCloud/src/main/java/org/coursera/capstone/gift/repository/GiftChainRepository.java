package org.coursera.capstone.gift.repository;

import org.coursera.capstone.gift.client.GiftEntitySvcApi;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@EnableScan 

@RepositoryRestResource(path=GiftEntitySvcApi.CHAIN_SVC_PATH)
public interface GiftChainRepository extends
		CrudRepository<GiftChainEntity, String> {

	
}
