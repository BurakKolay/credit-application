package com.burakkolay.credit.model.mapper;

import com.burakkolay.credit.model.DTO.CreditDTO;
import com.burakkolay.credit.model.entity.Credit;
import org.mapstruct.Mapper;

@Mapper
public interface CreditMapper {
    CreditDTO toCreditDTO(Credit credit);
    Credit toCredit(CreditDTO creditDTO);
}
