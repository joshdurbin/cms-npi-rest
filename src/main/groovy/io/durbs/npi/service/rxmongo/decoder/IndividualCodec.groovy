package io.durbs.npi.service.rxmongo.decoder

import io.durbs.npi.domain.Individual
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.Document
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.DocumentCodec
import org.bson.codecs.EncoderContext

class IndividualCodec implements Codec<Individual> {

  static final Codec<Document> documentCodec = new DocumentCodec()

  @Override
  Individual decode(BsonReader reader, DecoderContext decoderContext) {

    final Document document = documentCodec.decode(reader, decoderContext)

    new Individual(npiCode: document.getString('npiCode'),
      replacementCode: document.getString('replacementCode'),
      taxonomies: document.get('taxonomies', List).collect(CodecUtils.BSON_DOCUMENT_TO_TAXONOMY),
      otherProviderInformation: document.get('otherProviderInformation', List).collect(CodecUtils.BSON_DOCUMENT_TO_OTHER_PROVIDER_INFORMATION),
      mailingAddress: CodecUtils.BSON_DOCUMENT_TO_ADDRESS(document.get('mailingAddress', Document)),
      practiceAddress: CodecUtils.BSON_DOCUMENT_TO_ADDRESS(document.get('practiceAddress', Document)),
      providerEnumerationDate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'providerEnumerationDate'),
      lastUpdate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'lastUpdate'),
      npiDeactivationReasonCode: document.getString('npiDeactivationReasonCode'),
      npiDeactivationDate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'npiDeactivationDate'),
      npiReactivationDate: CodecUtils.BSON_DOCUMENT_TO_LOCALDATE(document, 'npiReactivationDate'),
      firstName: document.getString('firstName'),
      middleName: document.getString('middleName'),
      lastName: document.getString('lastName'),
      namePrefix: document.getString('namePrefix'),
      nameSuffix: document.getString('nameSuffix'),
      credentialText: document.getString('credentialText'),
      employerIdentificationNumber: document.getString('employerIdentificationNumber'),
      gender: document.getString('gender'),
      soleProprietor: document.getBoolean('soleProprietor'))
  }

  @Override
  void encode(BsonWriter writer, Individual value, EncoderContext encoderContext) {

    // DO NOTHING, DATA IS IMMUTABLE, READ ONLY
  }

  @Override
  Class<Individual> getEncoderClass() {
    Individual
  }
}
