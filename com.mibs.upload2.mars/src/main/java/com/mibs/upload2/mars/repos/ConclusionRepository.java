package com.mibs.upload2.mars.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mibs.upload2.mars.entity.Conclusion;

@Transactional
public interface ConclusionRepository extends CrudRepository<Conclusion, Long>{
	
	List<Conclusion> findByExplorationid(Long id);
	Conclusion findById(Long id);
	@Modifying 
	@Query("delete from Conclusion c WHERE c.explorationid = :explorationid")
	void removeConclusions(@Param("explorationid") Long explorationid);

}
