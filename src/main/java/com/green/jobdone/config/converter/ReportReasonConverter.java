package com.green.jobdone.config.converter;

import com.green.jobdone.entity.ReportReason;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReportReasonConverter implements AttributeConverter<ReportReason, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReportReason attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public ReportReason convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ReportReason.fromCode(dbData) : null;
    }
}