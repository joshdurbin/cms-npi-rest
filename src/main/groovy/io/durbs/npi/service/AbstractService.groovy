package io.durbs.npi.service

import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixObservableCommand
import io.durbs.npi.domain.Record
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO
import ratpack.exec.Blocking
import rx.Observable

abstract class AbstractService<T extends Record> {

  abstract BasicDAO<T, ObjectId> getDao()

  abstract HystrixCommandGroupKey getCommandGroupKey()

  Observable<Long> getCount() {

    new HystrixObservableCommand<Long>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Count'))) {

      @Override
      protected Observable<Long> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .countAll()

        }.observe()
      }

      @Override
      protected String getCacheKey() {
        'Count'
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()

  }

  Observable<T> getAll(final Integer pageNumber, final Integer pageSize) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('All'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .limit(pageSize)
            .offset(pageSize * pageNumber)
            .iterator()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "All-$pageNumber-$pageSize"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> getAllForPracticePostalCode(final String postalCode, final Integer pageNumber, final Integer pageSize) {


    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('GetAllForPracticePostalCode'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .field('practiceAddress.postalCode').equal(postalCode)
            .limit(pageSize)
            .offset(pageSize * pageNumber)
            .iterator()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "GetAllForPracticePostalCode-$postalCode-$pageNumber-$pageSize"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> getByNPICode(final String npiCode) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('GetByNPICode'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .field('npiCode').equal(npiCode)
            .get()

        }.observe()
      }

      @Override
      protected String getCacheKey() {
        "GetByNPICode-$npiCode"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> findByName(String searchTerm, final Integer pageNumber, final Integer pageSize) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('FindByName'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .search(searchTerm)
            .limit(pageSize)
            .offset(pageSize * pageNumber)
            .toList()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "findByName-$searchTerm-$pageNumber-$pageSize"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }
}
