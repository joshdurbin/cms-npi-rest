package io.durbs.npi.service

import com.google.inject.Singleton
import io.durbs.npi.domain.Individual
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO
import ratpack.exec.Blocking
import rx.Observable

import javax.inject.Inject

@Singleton
class IndividualService {

  @Inject
  private BasicDAO<Individual, ObjectId> individualDao

  Observable<Individual> getIndividuals(final Integer page, final Integer pageSize) {

    Blocking.get {
      individualDao.createQuery().limit(pageSize).offset(pageSize * page).iterator()
    }.observeEach()
  }

  Observable<Individual> getIndividualByCode(final String npiCode) {

    Blocking.get {
      individualDao.createQuery().field('npiCode').equal(npiCode).get()
    }.observe()
  }

}
