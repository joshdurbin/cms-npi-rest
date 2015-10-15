package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.mongodb.morphia.annotations.Embedded

@Canonical
@CompileStatic
@Embedded
class OtherProviderInformation {

  String identifier
  String typeCode
  String state
  String issuer
}
