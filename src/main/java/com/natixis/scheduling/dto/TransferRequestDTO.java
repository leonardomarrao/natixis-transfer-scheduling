package com.natixis.scheduling.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRequestDTO(
    @NotBlank(message = "A conta de origem é obrigatória.")
    String sourceAccount,

    @NotBlank(message = "A conta de destino é obrigatória.")
    String destinationAccount,

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor da transferência deve ser maior que zero.")
    BigDecimal amount,

    @NotNull(message = "A data da transferência é obrigatória.")
    @FutureOrPresent(message = "A data da transferência não pode estar no passado.")
    LocalDate transferDate
) {}