package io.durbs.npi.domain

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.mongodb.morphia.annotations.Embedded

@Immutable
@CompileStatic
@Embedded
class Taxonomy {

  Boolean isPrimaryTaxonomy
  String providerTaxonomyCode
  String providerLicenseNumber
  String providerLicenseNumberStateCode
  String taxonomyGroupCode
}
