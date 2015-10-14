package io.durbs.npi.service

import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoCollection
import com.mongodb.rx.client.MongoDatabase
import groovy.transform.CompileStatic
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Address
import io.durbs.npi.domain.OtherProviderInformation
import io.durbs.npi.domain.Taxonomy
import org.bson.Document

@CompileStatic
abstract class RxMongoPersistenceService {

  final MongoClient mongoClient
  final MongoDatabase db

  RxMongoPersistenceService(RxMongoPersistenceServiceConfig config) {
    mongoClient = MongoClients.create(config.uri)
    db = mongoClient.getDatabase(config.db)
  }

  abstract String getCollectionName()

  MongoCollection<Document> getCollection() {

    db.getCollection(getCollectionName())
  }

  protected final static BSON_DOCUMENT_TO_ADDRESS = { final Document document ->

    new Address(streetAddressLine1: document.getString('streetAddressLine1'),
      streetAddressLine2: document.getString('streetAddressLine2'),
      city: document.getString('city'),
      state: document.getString('state'),
      postalCode: document.getString('postalCode'),
      countryCode: document.getString('countryCode'),
      telephoneNumber: document.getString('telephoneNumber'),
      faxNumber: document.getString('faxNumber'))
  }

  protected final static BSON_DOCUMENT_TO_TAXONOMY = { final Document document ->

    new Taxonomy(isPrimaryTaxonomy: document.getBoolean('isPrimaryTaxonomy'),
      providerTaxonomyCode: document.getString('providerTaxonomyCode'),
      providerLicenseNumber: document.getString('providerLicenseNumber'),
      providerLicenseNumberStateCode: document.getString('providerLicenseNumberStateCode'),
      taxonomyGroupCode: document.getString('taxonomyGroupCode'))
  }

  protected final static BSON_DOCUMENT_TO_OTHER_PROVIDER_INFORMATION = { Document document ->

    new OtherProviderInformation(identifier: document.getString('identifier'),
      typeCode: document.getString('typeCode'),
      state: document.getString('state'),
      issuer: document.getString('issuer'))
  }
}
