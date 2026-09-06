package com.natixis.scheduling.service;

import com.natixis.scheduling.dto.TransferRequestDTO;
import com.natixis.scheduling.dto.TransferResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TransferService {
    TransferResponseDTO createTransfer(TransferRequestDTO dto);
    List<TransferResponseDTO> findAll();
    TransferResponseDTO findById(Long id);
    TransferResponseDTO updateTransfer(Long id, TransferRequestDTO dto);
    void deleteTransfer(Long id);
    List<TransferResponseDTO> findByAccount(String account);
    List<TransferResponseDTO> findByDateRange(LocalDate startDate, LocalDate endDate);
}