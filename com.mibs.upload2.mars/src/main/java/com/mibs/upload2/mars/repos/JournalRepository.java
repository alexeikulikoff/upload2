package com.mibs.upload2.mars.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mibs.upload2.mars.entity.Journal;

@Transactional
public interface JournalRepository extends CrudRepository<Journal, Long>{
	

	Journal findById(Long id);
	Journal findByUid(String uid);
	List<Journal> findByFlag(int flag); 
//	@Query("delete from Conclusion c WHERE c.explorationid = :explorationid")
//	void removeConclusions(@Param("explorationid") Long explorationid);
}
