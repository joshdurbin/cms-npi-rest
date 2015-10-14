package io.durbs.npi.config

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@Immutable
@CompileStatic
class RequestLimitsConfig {

  Integer defaultResultsPageSize
  Integer maxResultsPageSize
  Integer defaultFirstPage
}
