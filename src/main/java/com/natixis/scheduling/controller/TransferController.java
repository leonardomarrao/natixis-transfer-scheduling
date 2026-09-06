package com.natixis.scheduling.controller;

import com.natixis.scheduling.dto.TransferRequestDTO;
import com.natixis.scheduling.dto.TransferResponseDTO;
import com.natixis.scheduling.service.TransferService;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(@Valid @RequestBody TransferRequestDTO dto) {
        TransferResponseDTO response = service.createTransfer(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransferResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferResponseDTO> updateTransfer(
            @PathVariable Long id, 
            @Valid @RequestBody TransferRequestDTO dto
    ) {
        return ResponseEntity.ok(service.updateTransfer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        service.deleteTransfer(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/by-account")
    public ResponseEntity<List<TransferResponseDTO>> findByAccount(
            @RequestParam String account) {

        return ResponseEntity.ok(service.findByAccount(account));
    }
    
    @GetMapping("/by-date-range")
    public ResponseEntity<List<TransferResponseDTO>> findByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                service.findByDateRange(startDate, endDate)
        );
    }
}