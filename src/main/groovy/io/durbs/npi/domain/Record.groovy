package io.durbs.npi.domain

import groovy.transform.CompileStatic
import org.bson.types.ObjectId

import java.time.LocalDate

@CompileStatic
class Record {

  ObjectId id
  String npiCode
  String replacementCode
  LocalDate providerEnumerationDate
  LocalDate lastUpdate
  String npiDeactivationReasonCode
  LocalDate npiDeactivationDate
  LocalDate npiReactivationDate
  List<Taxonomy> taxonomies
  List<OtherProviderInformation> otherProviderInformation
  Address mailingAddress
  Address practiceAddress
}