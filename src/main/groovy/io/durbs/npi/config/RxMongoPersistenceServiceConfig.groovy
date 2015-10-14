package io.durbs.npi.config

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@Immutable
@CompileStatic
class RxMongoPersistenceServiceConfig {

  String db
  String uri
}
