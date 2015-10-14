package io.durbs.npi.service

import groovy.transform.CompileStatic
import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Organization
import org.bson.Document
import rx.Observable
import rx.functions.Func1

import javax.inject.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

import static com.mongodb.client.model.Filters.eq

class OrganizationService extends RxMongoPersistenceService {

  @Inject
  OrganizationService(RxMongoPersistenceServiceConfig config) {
    super(config)
  }

  Observable<Organization> getOrganizations(final Integer page, final Integer pageSize) {
    getCollection()
      .find()
      .limit(pageSize)
      .skip(pageSize * page)
      .toObservable()
      .map(DOCUMENT_TO_ORGANIZATION)
      .bindExec()
  }

  Observable<Organization> getOrganizationByCode(final String npiCode) {
    getCollection()
      .find(eq('npiCode', npiCode))
      .toObservable()
      .map(DOCUMENT_TO_ORGANIZATION)
      .bindExec()
  }

  Observable<Organization> getOrganizationsByPostalCode(final String postalCode, final Integer page, final Integer pageSize) {
    getCollection()
      .find(eq('practiceAddress.postalCode', postalCode))
      .limit(pageSize)
      .skip(pageSize * page)
      .toObservable()
      .map(DOCUMENT_TO_ORGANIZATION)
      .bindExec()
  }

  @Override
  String getCollectionName() {
    'organizations'
  }

  private static Func1<Document, Organization> DOCUMENT_TO_ORGANIZATION = { final Document document ->

    new Organization(npiCode: document.getString('npiCode'),
      replacementCode: document.getString('replacementCode'),
      taxonomies: document.get('taxonomies').collect(BSON_DOCUMENT_TO_TAXONOMY),
      otherProviderInformation: document.get('otherProviderInformation').collect(BSON_DOCUMENT_TO_OTHER_PROVIDER_INFORMATION),
      mailingAddress: null,
      practiceAddress: null,
      providerEnumerationDate: document.containsKey('providerEnumerationDate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('providerEnumerationDate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      lastUpdate: document.containsKey('lastUpdate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('lastUpdate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      npiDeactivationReasonCode: document.getString('npiDeactivationReasonCode'),
      npiDeactivationDate: document.containsKey('npiDeactivationDate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('npiDeactivationDate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      npiReactivationDate: document.containsKey('npiReactivationDate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('npiReactivationDate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      name: document.getString('name'),
      otherNames: document.get('otherName') as List,
      authorizedOfficial: null,
      subpart: document.getBoolean('subpart'))
  } as Func1
}
