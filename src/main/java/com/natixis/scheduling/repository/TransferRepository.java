package com.natixis.scheduling.repository;

import com.natixis.scheduling.model.Transfer;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository  extends JpaRepository<Transfer, Long>{
	List<Transfer> findBySourceAccountOrDestinationAccount(
	        String sourceAccount,
	        String destinationAccount
	);
	
	List<Transfer> findByTransferDateBetween(
	        LocalDate startDate,
	        LocalDate endDate
	);
}
