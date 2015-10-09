package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Address {

  String streetAddressLine1
  String streetAddressLine2
  String city
  String state
  String postalCode
  String countryCode
  String telephoneNumber
  String faxNumber
}
