/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents;

import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author malyshev
 */
@Converter(autoApply = false)
public class DateTimeConverter implements AttributeConverter<Date, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Date attribute) {//Convert to UTC (GMT) for database
        return attribute != null ? new Timestamp(attribute.getTime() - TimeZone.getDefault().getRawOffset()) : null;
    }

    @Override
    public Date convertToEntityAttribute(Timestamp dbData) {//Convert from UTC (GMT) for entity
        return dbData != null ? new Date(dbData.getTime() + TimeZone.getDefault().getRawOffset()) : null;
    }
}
