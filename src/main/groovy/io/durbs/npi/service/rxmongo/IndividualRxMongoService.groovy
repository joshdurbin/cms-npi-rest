package io.durbs.npi.service.rxmongo

import com.google.inject.Inject
import com.google.inject.Singleton
import com.netflix.hystrix.HystrixCommandGroupKey
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Individual
import io.durbs.npi.service.IndividualService

@Singleton
class IndividualRxMongoService extends AbstractRxMongoService<Individual> implements IndividualService {

  @Inject
  IndividualRxMongoService(RxMongoPersistenceServiceConfig config) {
    super(config, Individual)
  }

  @Override
  String getCollectionName() {
    'individuals'
  }

  @Override
  HystrixCommandGroupKey getCommandGroupKey() {
    HystrixCommandGroupKey.Factory.asKey('IndividualRxMongoService')
  }
}