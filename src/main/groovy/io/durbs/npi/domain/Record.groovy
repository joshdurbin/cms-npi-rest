package io.durbs.npi.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Embedded
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Indexed

@CompileStatic
class Record {

  @Id
  @JsonIgnore
  ObjectId id

  @Indexed(unique = true)
  String npiCode

  String replacementCode
  LocalDate providerEnumerationDate

  @Indexed
  LocalDate lastUpdate
  String npiDeactivationReasonCode
  LocalDate npiDeactivationDate
  LocalDate npiReactivationDate

  @Embedded
  List<Taxonomy> taxonomies

  @Embedded
  List<OtherProviderInformation> otherProviderInformation

  @Embedded
  Address mailingAddress

  @Embedded
  Address practiceAddress
}

import java.time.LocalDate
