package io.durbs.npi.service

import com.mongodb.ConnectionString
import com.mongodb.MongoClient
import com.mongodb.async.client.MongoClientSettings
import com.mongodb.connection.ClusterSettings
import com.mongodb.connection.ConnectionPoolSettings
import com.mongodb.connection.ServerSettings
import com.mongodb.connection.SocketSettings
import com.mongodb.connection.SslSettings
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoClient as RXMongoClient
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.MongoDatabase
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixObservableCommand
import groovy.transform.CompileStatic
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Record
import io.durbs.npi.service.codec.IndividualCodec
import io.durbs.npi.service.codec.OrganizationCodec
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import rx.Observable

import static com.mongodb.client.model.Filters.eq
import static com.mongodb.client.model.Filters.text

@CompileStatic
abstract class AbstractRxMongoService<T extends Record> {

  private MongoDatabase mongoDatabase
  private Class<T> clazz

  abstract String getCollectionName()

  abstract HystrixCommandGroupKey getCommandGroupKey()

  AbstractRxMongoService(RxMongoPersistenceServiceConfig config, Class<T> clazz) {

    this.clazz = clazz

    final ConnectionString connectionString = new ConnectionString(config.uri)
    final CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
      MongoClient.getDefaultCodecRegistry(),
      CodecRegistries.fromCodecs([new IndividualCodec(), new OrganizationCodec()]))

    final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
      .codecRegistry(codecRegistry)
      .clusterSettings(ClusterSettings.builder().applyConnectionString(connectionString).build())
      .connectionPoolSettings(ConnectionPoolSettings.builder().applyConnectionString(connectionString).build())
      .serverSettings(ServerSettings.builder().build()).credentialList(connectionString.getCredentialList())
      .sslSettings(SslSettings.builder().applyConnectionString(connectionString).build())
      .socketSettings(SocketSettings.builder().applyConnectionString(connectionString).build())
      .build()

    RXMongoClient rxMongoClient = MongoClients.create(mongoClientSettings)
    mongoDatabase = rxMongoClient.getDatabase(config.db)
  }

  MongoCollection<T> getCollection() {

   mongoDatabase.getCollection(getCollectionName(), clazz)
  }

  Observable<Long> getCount() {

    new HystrixObservableCommand<Long>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('Count'))) {

      @Override
      protected Observable<Long> construct() {

        getCollection()
          .count()
          .bindExec()
      }

      @Override
      protected String getCacheKey() {
        'Rx-Count'
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()

  }

  Observable<T> getAll(final RequestParameters requestParameters) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('All'))) {

      @Override
      protected Observable<T> construct() {

        getCollection()
          .find()
          .limit(requestParameters.pageSize)
          .skip(requestParameters.offSet)
          .toObservable()
          .bindExec()
      }

      @Override
      protected String getCacheKey() {
        "Rx-All-$requestParameters"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('GetAllForPracticePostalCode'))) {

      @Override
      protected Observable<T> construct() {

        getCollection()
          .find(eq('address.postalCode', postalCode))
          .limit(requestParameters.pageSize)
          .skip(requestParameters.offSet)
          .toObservable()
          .bindExec()
      }

      @Override
      protected String getCacheKey() {
        "Rx-GetAllForPracticePostalCode-$postalCode-$requestParameters"
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

        getCollection()
          .find(eq('npiCode', npiCode))
          .toObservable()
          .bindExec()
      }

      @Override
      protected String getCacheKey() {
        "Rx-GetByNPICode-$npiCode"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

  Observable<T> findByName(String searchTerm) {

    new HystrixObservableCommand<T>(HystrixObservableCommand.Setter.withGroupKey(getCommandGroupKey())
      .andCommandKey(HystrixCommandKey.Factory.asKey('FindByName'))) {

      @Override
      protected Observable<T> construct() {

        getCollection()
          .find(text(searchTerm))
          .toObservable()
          .bindExec()
      }

      @Override
      protected String getCacheKey() {
        "Rx-FindByName-$searchTerm"
      }

      @Override
      protected Observable<T> resumeWithFallback() {
        Observable.empty()
      }

    }.toObservable()
  }

}
