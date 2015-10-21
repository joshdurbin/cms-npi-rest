package io.durbs.npi.service.morphia

import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixObservableCommand
import io.durbs.npi.chain.ParametersChain
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Record
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO
import org.mongodb.morphia.query.Query
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

          updateQueryWithRequestParams(getDao()
            .createQuery(), requestParameters).fetch()

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

          updateQueryWithRequestParams(getDao()
            .createQuery()
            .field('practiceAddress.postalCode').equal(postalCode), requestParameters).fetch()

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

  Observable<T> findByName(String searchTerm, final RequestParameters requestParameters) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Morphia-FindByName'))) {

      @Override
      protected Observable<T> construct() {

        Blocking.get {

          updateQueryWithRequestParams(getDao()
            .createQuery()
            .search(searchTerm), requestParameters).iterator()

        }.observeEach()
      }

      @Override
      protected String getCacheKey() {
        "Morphia-FindByName-$searchTerm-$requestParameters"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  static Query<T> updateQueryWithRequestParams(Query<T> query, RequestParameters requestParameters) {

    query
      .limit(requestParameters.pageSize)
      .offset(requestParameters.offSet)
      .order(requestParameters.orderCriteria)

    if (requestParameters.status == ParametersChain.Status.active) {
      query.field('npiDeactivationDate').doesNotExist()
    } else if (requestParameters.status == ParametersChain.Status.inactive) {
      query.field('npiDeactivationDate').exists()
      query.field('npiReactivationDate').doesNotExist()
    } else {
      query.field('npiReactivationDate').exists()
    }

    query
  }
}
