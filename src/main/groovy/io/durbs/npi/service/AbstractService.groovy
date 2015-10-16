package io.durbs.npi.service

import io.durbs.npi.domain.Record
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO
import ratpack.exec.Blocking
import rx.Observable

abstract class AbstractService<T extends Record> {

  abstract BasicDAO<T, ObjectId> getDao()

  Observable<Long> getCount() {

    Blocking.get {
      getDao()
        .createQuery()
        .countAll()

    }.observe()
  }

  Observable<T> getAll(final Integer page, final Integer pageSize) {

    Blocking.get {
      getDao()
        .createQuery()
        .limit(pageSize)
        .offset(pageSize * page)
        .iterator()

    }.observeEach()
  }

  Observable<T> getAllForPracticePostalCode(final String postalCode, final Integer page, final Integer pageSize) {

    Blocking.get {
      getDao()
        .createQuery()
        .field('practiceAddress.postalCode').equal(postalCode)
        .limit(pageSize)
        .offset(pageSize * page)
        .iterator()

    }.observeEach()
  }

  Observable<T> getByNPICode(final String npiCode) {

    Blocking.get {
      getDao()
        .createQuery()
        .field('npiCode').equal(npiCode)
        .get()

    }.observe()
  }
}
