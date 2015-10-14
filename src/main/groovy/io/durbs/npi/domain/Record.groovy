package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

import java.time.LocalDate

@Canonical
@CompileStatic
class Record {

  String npiCode
  String replacementCode
  List<Taxonomy> taxonomies
  List<OtherProviderInformation> otherProviderInformation
  Address mailingAddress
  Address practiceAddress
  LocalDate providerEnumerationDate
  LocalDate lastUpdate
  String npiDeactivationReasonCode
  LocalDate npiDeactivationDate
  LocalDate npiReactivationDate
}
