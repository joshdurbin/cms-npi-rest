package io.durbs.npi.service

import groovy.transform.CompileStatic
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Individual
import rx.Observable

@CompileStatic
interface IndividualService {

  Observable<Long> getCount()

  Observable<Individual> getAll(final RequestParameters requestParameters)

  Observable<Individual> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters)

  Observable<Individual> getByNPICode(final String npiCode)

  Observable<Individual> findByName(final String searchTerm)

}
