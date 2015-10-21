package io.durbs.npi.service.morphia

import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixObservableCommand
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Record
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO
import ratpack.exec.Blocking
import rx.Observable

abstract class AbstractMorphiaService<T extends Record> {

  abstract BasicDAO<T, ObjectId> getDao()

  abstract HystrixCommandGroupKey getCommandGroupKey()

  Observable<Long> getCount() {

    new HystrixObservableCommand<Long>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Morphia-Count'))) {

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
        'Morphia-Count'
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()

  }

  Observable<T> getAll(final RequestParameters requestParameters) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Morphia-All'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .limit(requestParameters.pageNumber)
            .offset(requestParameters.offSet)
            .fetch()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "Morphia-All-$requestParameters"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Morphia-GetAllForPracticePostalCode'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .field('practiceAddress.postalCode').equal(postalCode)
            .limit(requestParameters.pageNumber)
            .offset(requestParameters.offSet)
            .fetch()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "Morphia-GetAllForPracticePostalCode-$postalCode-$requestParameters"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> getByNPICode(final String npiCode) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Morphia-GetByNPICode'))) {

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
        "Morphia-GetByNPICode-$npiCode"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> findByName(String searchTerm) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Morphia-FindByName'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          getDao()
            .createQuery()
            .search(searchTerm)
            .iterator()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "Morphia-FindByName-$searchTerm"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }
}
