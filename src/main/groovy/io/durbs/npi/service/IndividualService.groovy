package io.durbs.npi.service

import io.durbs.npi.config.RxMongoPersistenceServiceConfig
import io.durbs.npi.domain.Individual
import org.bson.Document
import rx.Observable
import rx.functions.Func1

import javax.inject.Inject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

import static com.mongodb.client.model.Filters.eq

class IndividualService extends RxMongoPersistenceService {

  @Inject
  IndividualService(RxMongoPersistenceServiceConfig config) {
    super(config)
  }

  Observable<Individual> getIndividuals(final Integer page, final Integer pageSize) {
    getCollection()
      .find()
      .limit(pageSize)
      .skip(pageSize * page)
      .toObservable()
      .map(DOCUMENT_TO_INDIVIDUAL)
      .bindExec()
  }

  Observable<Individual> getIndividualByCode(final String npiCode) {
    getCollection()
      .find(eq('npiCode', npiCode))
      .toObservable()
      .map(DOCUMENT_TO_INDIVIDUAL)
      .bindExec()
  }

  @Override
  String getCollectionName() {
    'individuals'
  }

  private static Func1<Document, Individual> DOCUMENT_TO_INDIVIDUAL = { final Document document ->

    new Individual(npiCode: document.getString('npiCode'),
      replacementCode: document.getString('replacementCode'),
      taxonomies: document.get('taxonomies', List).collect(BSON_DOCUMENT_TO_TAXONOMY),
      otherProviderInformation: document.get('otherProviderInformation', List).collect(BSON_DOCUMENT_TO_OTHER_PROVIDER_INFORMATION),
      mailingAddress: BSON_DOCUMENT_TO_ADDRESS(document.get('mailingAddress', Document)),
      practiceAddress: BSON_DOCUMENT_TO_ADDRESS(document.get('practiceAddress', Document)),
      providerEnumerationDate: document.containsKey('providerEnumerationDate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('providerEnumerationDate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      lastUpdate: document.containsKey('lastUpdate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('lastUpdate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      npiDeactivationReasonCode: document.getString('npiDeactivationReasonCode'),
      npiDeactivationDate: document.containsKey('npiDeactivationDate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('npiDeactivationDate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      npiReactivationDate: document.containsKey('npiReactivationDate') ? LocalDateTime.ofInstant(Instant.ofEpochMilli(document.getDate('npiReactivationDate').getTime()), ZoneId.systemDefault()).toLocalDate() : null,
      firstName: document.getString('firstName'),
      middleName: document.getString('middleName'),
      lastName: document.getString('lastName'),
      namePrefix: document.getString('namePrefix'),
      nameSuffix: document.getString('nameSuffix'),
      credentialText: document.getString('credentialText'),
      employerIdentificationNumber: document.getString('employerIdentificationNumber'),
      gender: document.getString('gender'),
      soleProprietor: document.getBoolean('soleProprietor'))
  } as Func1

}
