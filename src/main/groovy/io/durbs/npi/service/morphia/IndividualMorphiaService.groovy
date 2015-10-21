package io.durbs.npi.service.morphia

import com.netflix.hystrix.HystrixCommandGroupKey
import io.durbs.npi.domain.Individual
import com.google.inject.Singleton
import io.durbs.npi.service.IndividualService
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO

import javax.inject.Inject

@Singleton
class IndividualMorphiaService extends AbstractMorphiaService<Individual> implements IndividualService {

  @Inject
  private BasicDAO<Individual, ObjectId> individualDao

  @Override
  BasicDAO getDao() {
    individualDao
  }

  @Override
  HystrixCommandGroupKey getCommandGroupKey() {
    HystrixCommandGroupKey.Factory.asKey('IndividualMorphiaService')
  }
}
