package io.durbs.npi.domain

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
class Taxonomy {

  Boolean isPrimaryTaxonomy
  String providerTaxonomyCode
  String providerLicenseNumber
  String providerLicenseNumberStateCode
  String taxonomyGroupCode
}
