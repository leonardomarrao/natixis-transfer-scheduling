package com.natixis.scheduling.repository;

import com.natixis.scheduling.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository  extends JpaRepository<Transfer, Long>{
	
}
