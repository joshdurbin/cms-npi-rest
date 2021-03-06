package io.durbs.npi.service

import com.google.inject.Inject
import com.google.inject.Singleton
import groovy.transform.CompileStatic
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Organization

@Singleton
@CompileStatic
class OrganizationService extends AbstractRxMongoService<Organization> {

  @Inject
  OrganizationService(RxMongoPersistenceServiceConfig config) {
    super(config, Organization)
  }

  @Override
  String getCollectionName() {
    'organizations'
  }

}