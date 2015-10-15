package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.utils.IndexType

@Canonical
@CompileStatic
@Entity('individuals')
@Indexes(
  @Index(name = 'Individual Name Text Index', fields = [
    @Field(value = 'firstName', type = IndexType.TEXT),
    @Field(value = 'middleName', type = IndexType.TEXT),
    @Field(value = 'lastName', type = IndexType.TEXT)]))
class Individual extends Record {

  String firstName
  String middleName
  String lastName
  String namePrefix
  String nameSuffix
  String credentialText
  String employerIdentificationNumber
  String gender
  Boolean soleProprietor
}
