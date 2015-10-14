package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Organization extends Record {

  String name
  List<String> otherNames
  AuthorizedOfficial authorizedOfficial
  Boolean subpart
}
