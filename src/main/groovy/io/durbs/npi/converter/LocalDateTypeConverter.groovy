package io.durbs.npi.converter

import groovy.transform.CompileStatic
import org.mongodb.morphia.converters.SimpleValueConverter
import org.mongodb.morphia.converters.TypeConverter
import org.mongodb.morphia.mapping.MappedField

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@CompileStatic
class LocalDateTypeConverter extends TypeConverter implements SimpleValueConverter {

  public LocalDateTypeConverter() {
    super(LocalDate)
  }

  @Override
  public Object decode(final Class<?> targetClass, final Object val, final MappedField optionalExtraInfo) {

    if (val instanceof Date) {
      LocalDateTime.ofInstant(Instant.ofEpochMilli(val.getTime()), ZoneId.systemDefault()).toLocalDate()
    } else {
      throw new IllegalArgumentException("Can't convert to Date from " + val);
    }
  }
}