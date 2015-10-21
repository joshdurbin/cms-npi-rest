package io.durbs.npi.service.rxmongo.decoder

import groovy.transform.CompileStatic
import io.durbs.npi.domain.Address
import io.durbs.npi.domain.OtherProviderInformation
import io.durbs.npi.domain.Taxonomy
import org.bson.Document

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@CompileStatic
class CodecUtils {

  static final LocalDate BSON_DOCUMENT_TO_LOCALDATE(final Document document, final String propertyKey) {
    document.containsKey(propertyKey) ? LocalDateTime.ofInstant(
      Instant.ofEpochMilli(document.getDate(propertyKey).getTime()),
      ZoneId.systemDefault()).toLocalDate() : null
  }

  static final Address BSON_DOCUMENT_TO_ADDRESS(final Document document) {

    new Address(streetAddressLine1: document.getString('streetAddressLine1'),
      streetAddressLine2: document.getString('streetAddressLine2'),
      city: document.getString('city'),
      state: document.getString('state'),
      postalCode: document.getString('postalCode'),
      countryCode: document.getString('countryCode'),
      telephoneNumber: document.getString('telephoneNumber'),
      faxNumber: document.getString('faxNumber'))
  }

  final static BSON_DOCUMENT_TO_TAXONOMY = { final Document document ->

    new Taxonomy(isPrimaryTaxonomy: document.getBoolean('isPrimaryTaxonomy'),
      providerTaxonomyCode: document.getString('providerTaxonomyCode'),
      providerLicenseNumber: document.getString('providerLicenseNumber'),
      providerLicenseNumberStateCode: document.getString('providerLicenseNumberStateCode'),
      taxonomyGroupCode: document.getString('taxonomyGroupCode'))
  }

  final static BSON_DOCUMENT_TO_OTHER_PROVIDER_INFORMATION = { Document document ->

    new OtherProviderInformation(identifier: document.getString('identifier'),
      typeCode: document.getString('typeCode'),
      state: document.getString('state'),
      issuer: document.getString('issuer'))
  }
}
