package io.durbs.npi.service.rxmongo

import com.google.inject.Inject
import com.google.inject.Singleton
import com.netflix.hystrix.HystrixCommandGroupKey
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Organization
import io.durbs.npi.service.OrganizationService

@Singleton
class OrganizationRxMongoService extends AbstractRxMongoService<Organization> implements OrganizationService {

  @Inject
  OrganizationRxMongoService(RxMongoPersistenceServiceConfig config) {
    super(config, Organization)
  }

  @Override
  String getCollectionName() {
    'organizations'
  }

  @Override
  HystrixCommandGroupKey getCommandGroupKey() {
    HystrixCommandGroupKey.Factory.asKey('OrganizationRxMongoService')
  }
}