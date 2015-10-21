package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.mongodb.morphia.annotations.Embedded
import org.mongodb.morphia.annotations.Indexed

@Canonical
@CompileStatic
@Embedded
class Address {

  String streetAddressLine1
  String streetAddressLine2
  String city
  String state

  @Indexed
  String postalCode
  String countryCode
  String telephoneNumber
  String faxNumber
}
