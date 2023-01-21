package com.burakkolay.credit.model.mapper;

import com.burakkolay.credit.model.DTO.ApplicantDTO;
import com.burakkolay.credit.model.entity.Applicant;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Configuration;

@Mapper
public interface ApplicantMapper {
    ApplicantDTO toApplicantDTO(Applicant applicant);
    Applicant toApplicant(ApplicantDTO applicantDTO);
}
