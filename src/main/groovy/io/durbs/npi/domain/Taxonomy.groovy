package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.mongodb.morphia.annotations.Embedded

@Canonical
@CompileStatic
@Embedded
class Taxonomy {

  Boolean isPrimaryTaxonomy
  String providerTaxonomyCode
  String providerLicenseNumber
  String providerLicenseNumberStateCode
  String taxonomyGroupCode
}
