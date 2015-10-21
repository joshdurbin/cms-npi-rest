package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class OtherProviderInformation {

  String identifier
  String typeCode
  String state
  String issuer
}
