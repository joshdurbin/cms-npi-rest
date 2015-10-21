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

    getCollection()
      .count()
      .bindExec()
  }

  Observable<T> getAll(final RequestParameters requestParameters) {

    getCollection()
      .find()
      .limit(requestParameters.pageSize)
      .skip(requestParameters.offSet)
      .toObservable()
      .bindExec()
  }

  Observable<T> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters) {

    getCollection()
      .find(eq('address.postalCode', postalCode))
      .limit(requestParameters.pageSize)
      .skip(requestParameters.offSet)
      .toObservable()
      .bindExec()
  }

  Observable<T> getByNPICode(final String npiCode) {

    getCollection()
      .find(eq('npiCode', npiCode))
      .toObservable()
      .bindExec()
  }

  Observable<T> findByName(String searchTerm) {

    getCollection()
      .find(text(searchTerm))
      .toObservable()
      .bindExec()
  }

}
