package io.durbs.npi.service.codec

import io.durbs.npi.domain.AuthorizedOfficial
import io.durbs.npi.domain.Organization
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.Document
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.DocumentCodec
import org.bson.codecs.EncoderContext

class OrganizationCodec implements Codec<Organization> {

  static final Codec<Document> documentCodec = new DocumentCodec()

  @Override
  Organization decode(BsonReader reader, DecoderContext decoderContext) {

    final Document document = documentCodec.decode(reader, decoderContext)

    final Document authorizedOfficialDocument = document.get('authorizedOfficial', Document)
    final AuthorizedOfficial authorizedOfficial = new AuthorizedOfficial(firstName: authorizedOfficialDocument.getString('firstName'),
      middleName: authorizedOfficialDocument.getString('middleName'),
      lastName: authorizedOfficialDocument.getString('lastName'),
      namePrefix: authorizedOfficialDocument.getString('namePrefix'),
      nameSuffix: authorizedOfficialDocument.getString('nameSuffix'),
      credentialText: authorizedOfficialDocument.getString('credentialText'),
      titleOrPosition: authorizedOfficialDocument.getString('titleOrPosition'),
      telephoneNumber: authorizedOfficialDocument.getString('telephoneNumber'))

    new Organization(npiCode: document.getString('npiCode'),
      replacementCode: document.getString('replacementCode'),
      taxonomies: document.get('taxonomies').collect(CodecUtils.BSON_DOCUMENT_TO_TAXONOMY),
      otherProviderInformation: document.get('otherProviderInformation').collect(CodecUtils.BSON_DOCUMENT_TO_OTHER_PROVIDER_INFORMATION),
      mailingAddress: CodecUtils.BSON_DOCUMENT_TO_ADDRESS(document.get('mailingAddress', Document)),
      practiceAddress: CodecUtils.BSON_DOCUMENT_TO_ADDRESS(document.get('practiceAddress', Document)),
      providerEnumerationDate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'providerEnumerationDate'),
      lastUpdate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'lastUpdate'),
      npiDeactivationReasonCode: document.getString('npiDeactivationReasonCode'),
      npiDeactivationDate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'npiDeactivationDate'),
      npiReactivationDate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'npiReactivationDate'),
      name: document.getString('name'),
      otherName: document.get('otherName'),
      authorizedOfficial: authorizedOfficial,
      subpart: document.getBoolean('subpart'))
  }

  @Override
  void encode(BsonWriter writer, Organization organization, EncoderContext encoderContext) {

    // DO NOTHING, DATA IS IMMUTABLE, READ ONLY
  }

  @Override
  Class<Organization> getEncoderClass() {
    Organization
  }
}
