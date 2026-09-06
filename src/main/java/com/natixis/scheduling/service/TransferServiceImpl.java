package com.natixis.scheduling.service;

import com.natixis.scheduling.dto.TransferRequestDTO;
import com.natixis.scheduling.dto.TransferResponseDTO;
import com.natixis.scheduling.exception.ResourceNotFoundException;
import com.natixis.scheduling.model.Transfer;
import com.natixis.scheduling.repository.TransferRepository;
import com.natixis.scheduling.strategy.FeeContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;
    private final FeeContext feeContext;

    public TransferServiceImpl(TransferRepository repository, FeeContext feeContext) {
        this.repository = repository;
        this.feeContext = feeContext;
    }

    @Override
    @Transactional
    public TransferResponseDTO createTransfer(TransferRequestDTO dto) {
        Transfer transfer = new Transfer();
        transfer.setSourceAccount(dto.sourceAccount());
        transfer.setDestinationAccount(dto.destinationAccount());
        transfer.setAmount(dto.amount());
        transfer.setTransferDate(dto.transferDate());
        transfer.setScheduledDate(LocalDate.now());

        BigDecimal fee = feeContext.calculateFee(transfer);
        transfer.setFee(fee);

        Transfer savedTransfer = repository.save(transfer);
        return toResponseDTO(savedTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransferResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TransferResponseDTO findById(Long id) {
        Transfer transfer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado para o ID: " + id));
        return toResponseDTO(transfer);
    }

    @Override
    @Transactional
    public TransferResponseDTO updateTransfer(Long id, TransferRequestDTO dto) {
        Transfer transfer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado para o ID: " + id));

        transfer.setSourceAccount(dto.sourceAccount());
        transfer.setDestinationAccount(dto.destinationAccount());
        transfer.setAmount(dto.amount());
        transfer.setTransferDate(dto.transferDate());

        BigDecimal fee = feeContext.calculateFee(transfer);
        transfer.setFee(fee);

        Transfer updatedTransfer = repository.save(transfer);
        return toResponseDTO(updatedTransfer);
    }

    @Override
    @Transactional
    public void deleteTransfer(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Agendamento não encontrado para o ID: " + id);
        }
        repository.deleteById(id);
    }

    private TransferResponseDTO toResponseDTO(Transfer transfer) {
        return new TransferResponseDTO(
            transfer.getId(),
            transfer.getSourceAccount(),
            transfer.getDestinationAccount(),
            transfer.getAmount(),
            transfer.getFee(),
            transfer.getScheduledDate(),
            transfer.getTransferDate()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransferResponseDTO> findByAccount(String account) {
        return repository
                .findBySourceAccountOrDestinationAccount(account, account)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransferResponseDTO> findByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                "A data inicial não pode ser posterior à data final."
            );
        }

        return repository
                .findByTransferDateBetween(startDate, endDate)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}