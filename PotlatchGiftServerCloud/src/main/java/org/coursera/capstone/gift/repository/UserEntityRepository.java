package org.coursera.capstone.gift.repository;

import java.util.Collection;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@EnableScan 
@EnableScanCount
@RepositoryRestResource(path="/user")
public interface UserEntityRepository extends PagingAndSortingRepository<UserEntity, String>{
	public Collection<UserEntity> findByName(String name);
	
	
	
}
