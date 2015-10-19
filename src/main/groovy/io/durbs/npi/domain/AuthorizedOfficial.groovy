package io.durbs.npi.domain

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.mongodb.morphia.annotations.Embedded

@Immutable
@CompileStatic
@Embedded
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
