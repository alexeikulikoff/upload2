package com.mibs.upload2.mars.repos;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.mibs.upload2.mars.entity.Payments;


public interface PaymentsRepository extends CrudRepository<Payments, Long>{
	List<Payments> findByUserid(Long id);
	Payments findById(Long id);
}
