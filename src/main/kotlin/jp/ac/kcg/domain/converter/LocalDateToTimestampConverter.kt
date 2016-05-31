package jp.ac.kcg.domain.converter

import java.sql.Timestamp
import java.time.LocalDate
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class LocalDateToTimestampConverter: AttributeConverter<LocalDate, Timestamp> {
    override fun convertToDatabaseColumn(attribute: LocalDate?): Timestamp? {
        return attribute?.run { Timestamp.valueOf(atStartOfDay()) }
    }

    override fun convertToEntityAttribute(dbData: Timestamp?): LocalDate? {
        return if(dbData == null) null else dbData.toLocalDateTime().toLocalDate()
    }
}
