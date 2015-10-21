package io.durbs.npi.service

import groovy.transform.CompileStatic
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Organization
import rx.Observable

@CompileStatic
interface OrganizationService {

  Observable<Long> getCount()

  Observable<Organization> getAll(final RequestParameters requestParameters)

  Observable<Organization> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters)

  Observable<Organization> getByNPICode(final String npiCode)

  Observable<Organization> findByName(final String searchTerm)

}