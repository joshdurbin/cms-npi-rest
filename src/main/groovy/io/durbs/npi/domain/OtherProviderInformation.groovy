package io.durbs.npi.domain

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.mongodb.morphia.annotations.Embedded

@Immutable
@CompileStatic
@Embedded
class OtherProviderInformation {

  String identifier
  String typeCode
  String state
  String issuer
}
