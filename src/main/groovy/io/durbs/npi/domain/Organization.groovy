package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Organization extends Record {

  String name
  String otherName
  Boolean subpart
  AuthorizedOfficial authorizedOfficial
}
