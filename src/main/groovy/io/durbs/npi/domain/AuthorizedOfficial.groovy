package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class AuthorizedOfficial {

  String firstName
  String middleName
  String lastName
  String namePrefix
  String nameSuffix
  String credentialText
  String titleOrPosition
  String telephoneNumber
}
