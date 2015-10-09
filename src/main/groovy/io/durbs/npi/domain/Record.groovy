package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

import java.time.LocalDate

@Canonical
@CompileStatic
class Record {

  def type
  String npiCode
  String replacementCode
  List<Taxonomy> taxonomies
  OtherProviderInformation otherProviderInformation
  Address mailingAddress
  Address practiceAddress
  LocalDate providerEnumerationDate
  LocalDate lastUpdate
  def npiDeactivationReasonCode
  LocalDate npiDeactivationDate
  LocalDate npiReactivationDate
}
