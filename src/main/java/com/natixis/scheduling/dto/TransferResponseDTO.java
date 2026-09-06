package com.natixis.scheduling.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferResponseDTO(
    Long id,
    String sourceAccount,
    String destinationAccount,
    BigDecimal amount,
    BigDecimal fee,
    LocalDate scheduledDate,
    LocalDate transferDate
) {}