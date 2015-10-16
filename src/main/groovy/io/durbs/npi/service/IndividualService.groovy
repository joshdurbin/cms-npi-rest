package io.durbs.npi.service

import io.durbs.npi.domain.Individual
import com.google.inject.Singleton
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO

import javax.inject.Inject

@Singleton
class IndividualService extends AbstractService<Individual> {

  @Inject
  private BasicDAO<Individual, ObjectId> individualDao

  @Override
  BasicDAO getDao() {
    individualDao
  }
}
