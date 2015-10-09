@Grapes([
	@Grab(group='com.univocity', module='univocity-parsers', version='1.5.6'),
  @Grab(group='com.fasterxml.jackson.core', module='jackson-databind', version='2.6.2')
])

import com.univocity.parsers.csv.*

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import java.time.format.DateTimeFormatter
import java.time.LocalDate

def dateFormatter = DateTimeFormatter.ofPattern('MM/dd/yyyy')

class IsoDateSerializer extends StdSerializer<LocalDate> {

  public IsoDateSerializer(Class<LocalDate> t) {
    super(t)
  }

  @Override
  public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
    jsonGenerator.writeRawValue("ISODate(\"${localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}T00:00:00Z\")")
  }
}

def isoDateConversionModule = new SimpleModule('ISO Date Conversion MODULE');
isoDateConversionModule.addSerializer(new IsoDateSerializer(LocalDate))

def jsonMapper = new ObjectMapper()
jsonMapper.setSerializationInclusion(Include.NON_NULL)
jsonMapper.registerModule(isoDateConversionModule)

class Record {
	def npiCode
	def replacementCode
	def taxonomies = []
	def otherProviderInformation = []
	def mailingAddress
	def practiceAddress
	def providerEnumerationDate
	def lastUpdate
	def npiDeactivationReasonCode
	def npiDeactivationDate
	def npiReactivationDate
}

class Person extends Record {
	def firstName
	def middleName
	def lastName
	def namePrefix
	def nameSuffix
	def credentialText
}

class Individual extends Person {
	def employerIdentificationNumber
	def gender
	def soleProprietor
}

class Organization extends Record {
	def name
	def otherName
  def authorizedOfficial
	def subpart
}

class AuthorizedOfficial extends Person {
	def titleOrPosition
	def telephoneNumber
}

class Address {
	def streetAddressLine1
	def streetAddressLine2
	def city
	def state
	def postalCode
	def countryCode
	def telephoneNumber
	def faxNumber
}

class Taxonomy {
	def isPrimaryTaxonomy
	def providerTaxonomyCode
	def providerLicenseNumber
	def providerLicenseNumberStateCode
	def taxonomyGroupCode
}

class OtherProviderInformation {
	def identifier
	def typeCode
	def state
	def issuer
}

def generateAddress = { stringArray, generateMailingAddress ->

	new Address(
		streetAddressLine1: generateMailingAddress ? stringArray[20] : stringArray[20 + 8],
		streetAddressLine2: generateMailingAddress ? stringArray[21] : stringArray[21 + 8],
		city: generateMailingAddress ? stringArray[22] : stringArray[22 + 8],
		state: generateMailingAddress ? stringArray[23] : stringArray[23 + 8],
		postalCode: generateMailingAddress ? stringArray[24] : stringArray[24 + 8],
		countryCode: generateMailingAddress ? stringArray[25] : stringArray[25 + 8],
		telephoneNumber: generateMailingAddress ? stringArray[26] : stringArray[26 + 8],
		faxNumber: generateMailingAddress ? stringArray[27] : stringArray[27 + 8]
	)
}

def generateTaxonomies = {stringArray ->

	def taxonomyStartPosition = 47
	def taxonomyGroupCodeStartPosition = 314

	def results = []

	15.times {

		def providerTaxonomyCode = stringArray[taxonomyStartPosition++]
		def providerLicenseNumber = stringArray[taxonomyStartPosition++]
		def providerLicenseNumberStateCode = stringArray[taxonomyStartPosition++]
		def taxonomySwitch = stringArray[taxonomyStartPosition++]
		def taxonomyGroupCode = stringArray[taxonomyGroupCodeStartPosition++]

		if (providerTaxonomyCode || providerLicenseNumber || providerLicenseNumberStateCode || taxonomySwitch || taxonomyGroupCode) {

			results.add(new Taxonomy(
				providerTaxonomyCode: providerTaxonomyCode,
				providerLicenseNumber: providerLicenseNumber,
				providerLicenseNumberStateCode: providerLicenseNumberStateCode,
				isPrimaryTaxonomy: taxonomySwitch.toBoolean(),
				taxonomyGroupCode: taxonomyGroupCode))
		}
	}

	results
}

def generateOtherProviderInformation = { stringArray ->

	def otherProviderStartPosition = 107

	def results = []

	50.times {

		def identifier = stringArray[otherProviderStartPosition++]
		def typeCode = stringArray[otherProviderStartPosition++]
		def state = stringArray[otherProviderStartPosition++]
		def issuer = stringArray[otherProviderStartPosition++]

		if (identifier || typeCode || state || issuer) {

			results.add(new OtherProviderInformation(
				identifier: identifier,
				typeCode: typeCode,
				state: state,
				issuer: issuer))
		}
	}

	results
}

def settings = new CsvParserSettings()
settings.getFormat().setLineSeparator('\n')

def parser = new CsvParser(settings)

new File('individuals.json').withWriter { individualsJSONWriter ->
	new File('organizations.json').withWriter { organizationsJSONWriter ->

		new File('npidata_20050523-20150913.csv').eachLine { line, lineNumber ->

		  if (lineNumber % 100000 == 0) {
				println "Processing line number $lineNumber..."
			}

			def parsedValues = parser.parseLine(line)

			if (parsedValues[1] == '1') {

				individualsJSONWriter.writeLine(
					jsonMapper.writeValueAsString(
						new Individual(
							firstName: parsedValues[6],
							middleName: parsedValues[7],
							lastName: parsedValues[5],
							namePrefix: parsedValues[8],
							nameSuffix: parsedValues[9],
							credentialText: parsedValues[10],
							employerIdentificationNumber: parsedValues[3],
							gender: parsedValues[41],
							npiCode:  parsedValues[0],
							replacementCode: parsedValues[2],
							providerEnumerationDate: parsedValues[36] ? LocalDate.parse(parsedValues[36], dateFormatter) : null,
							lastUpdate: parsedValues[37] ? LocalDate.parse(parsedValues[37], dateFormatter) : null,
							npiDeactivationReasonCode: parsedValues[38],
							npiDeactivationDate: parsedValues[39] ? LocalDate.parse(parsedValues[39], dateFormatter) : null,
							npiReactivationDate: parsedValues[40] ? LocalDate.parse(parsedValues[40], dateFormatter) : null,
							soleProprietor: parsedValues[307]?.toBoolean(),
							taxonomies: generateTaxonomies(parsedValues),
							otherProviderInformation: generateOtherProviderInformation(parsedValues),
							mailingAddress: generateAddress(parsedValues, true),
							practiceAddress: generateAddress(parsedValues, false))))

			} else if (parsedValues[1] == '2') {

				def authorizedOfficial = new AuthorizedOfficial(
					firstName: parsedValues[43],
					middleName: parsedValues[44],
					lastName: parsedValues[42],
					namePrefix: parsedValues[311],
					nameSuffix: parsedValues[312],
					credentialText: parsedValues[313],
					titleOrPosition: parsedValues[45],
					telephoneNumber: parsedValues[46])

				organizationsJSONWriter.writeLine(
					jsonMapper.writeValueAsString(

						new Organization(
							name: parsedValues[4],
							otherName: parsedValues[],
							npiCode:  parsedValues[0],
							replacementCode: parsedValues[2],
							authorizedOfficial: authorizedOfficial,
							providerEnumerationDate: parsedValues[36] ? LocalDate.parse(parsedValues[36], dateFormatter) : null,
							lastUpdate: parsedValues[37] ? LocalDate.parse(parsedValues[37], dateFormatter) : null,
							npiDeactivationReasonCode: parsedValues[38],
							npiDeactivationDate: parsedValues[39] ? LocalDate.parse(parsedValues[39], dateFormatter) : null,
							npiReactivationDate: parsedValues[40] ? LocalDate.parse(parsedValues[40], dateFormatter) : null,
							subpart: parsedValues[308]?.toBoolean(),
							taxonomies: generateTaxonomies(parsedValues),
							otherProviderInformation: generateOtherProviderInformation(parsedValues),
							mailingAddress: generateAddress(parsedValues, true),
							practiceAddress: generateAddress(parsedValues, false))))
			}
		}
	}
}
