package com.mibs.upload2.mars.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mibs.upload2.mars.entity.Exploration;


@Transactional
public interface ExplorationRepository  extends CrudRepository<Exploration, Long>{
	List<Exploration> findAll();
	List<Exploration> findByUsersId(Long id);
	Exploration findById(Long id);
	Exploration findByUniqueid(String uid);
	

}
