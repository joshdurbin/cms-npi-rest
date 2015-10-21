package io.durbs.npi.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.netflix.hystrix.HystrixCommandGroupKey
import groovy.transform.CompileStatic
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Individual

@Singleton
@CompileStatic
class IndividualService extends AbstractRxMongoService<Individual> {

  @Inject
  IndividualService(RxMongoPersistenceServiceConfig config) {
    super(config, Individual)
  }

  @Override
  String getCollectionName() {
    'individuals'
  }

  @Override
  HystrixCommandGroupKey getCommandGroupKey() {
    HystrixCommandGroupKey.Factory.asKey('IndividualService')
  }
}