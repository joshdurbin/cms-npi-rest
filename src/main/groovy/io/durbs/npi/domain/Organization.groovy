package io.durbs.npi.domain

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.mongodb.morphia.annotations.Embedded
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.utils.IndexType

@Immutable
@CompileStatic
@Entity('organizations')
@Indexes(
  @Index(name = 'Organization Name Text Index', fields = @Field(value = 'name', type = IndexType.TEXT)))
class Organization extends Record {

  String name

  String otherName
  Boolean subpart

  @Embedded
  AuthorizedOfficial authorizedOfficial
}
