package com.mibs.upload2.mars.repos;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.mibs.upload2.mars.entity.Request;

public interface RequestRepository extends CrudRepository<Request, Long>{
	
	List<Request> findAll();
	List<Request> findByCurdateBetween(Long d1, Long d2);
	Request findByUid(String uid);

}
