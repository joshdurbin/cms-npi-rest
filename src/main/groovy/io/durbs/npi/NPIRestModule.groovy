package io.durbs.npi

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import groovy.transform.CompileStatic
import io.durbs.npi.chain.IndividualChain
import io.durbs.npi.chain.OrganizationChain
import io.durbs.npi.chain.ParametersChain
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.converter.LocalDateTypeConverter
import io.durbs.npi.domain.Individual
import io.durbs.npi.domain.Organization
import io.durbs.npi.renderer.IndividualRenderer
import io.durbs.npi.renderer.OrganizationRenderer
import io.durbs.npi.service.IndividualService
import io.durbs.npi.service.OrganizationService
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import org.mongodb.morphia.dao.BasicDAO

import javax.inject.Inject

@CompileStatic
class NPIRestModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(ParametersChain)

    bind(IndividualRenderer)
    bind(IndividualService)
    bind(IndividualChain)
    bind(OrganizationService)
    bind(OrganizationRenderer)
    bind(OrganizationChain)
  }

  @Provides
  @Singleton
  MongoClient provideMongoClient(RxMongoPersistenceServiceConfig config) {
    new MongoClient(new MongoClientURI(config.uri))
  }

  @Provides
  @Singleton
  Morphia provideMorphia() {

    final Morphia morphia = new Morphia()

    morphia.getMapper().getConverters().addConverter(LocalDateTypeConverter)
    morphia.mapPackage('io.durbs.npi.domain')
  }

  @Provides
  @Singleton
  @Inject
  Datastore provideDatastore(MongoClient mongoClient, Morphia morphia, RxMongoPersistenceServiceConfig config) {
    Datastore datastore = morphia.createDatastore(mongoClient, config.db)

    datastore.ensureIndexes(true)

    datastore
  }

  @Provides
  @Singleton
  @Inject
  BasicDAO<Individual, ObjectId> provideIndividualDao(Datastore datastore) {
    new BasicDAO<Individual, ObjectId>(Individual, datastore)
  }

  @Provides
  @Singleton
  @Inject
  BasicDAO<Organization, ObjectId> provideOrganizationDao(Datastore datastore) {
    new BasicDAO<Organization, ObjectId>(Organization, datastore)
  }

}
