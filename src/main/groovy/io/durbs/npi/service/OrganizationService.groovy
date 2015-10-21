package io.durbs.npi.service

import groovy.transform.CompileStatic
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Organization
import rx.Observable

@CompileStatic
interface OrganizationService {

  /**
   *
   * @return
   */
  Observable<Long> getCount()

  /**
   *
   * @param requestParameters
   * @return
   */
  Observable<Organization> getAll(final RequestParameters requestParameters)

  /**
   *
   * @param postalCode
   * @param requestParameters
   * @return
   */
  Observable<Organization> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters)

  /**
   *
   * @param npiCode
   * @return
   */
  Observable<Organization> getByNPICode(final String npiCode)

  /**
   *
   * @param searchTerm
   * @param requestParameters
   * @return
   */
  Observable<Organization> findByName(final String searchTerm, final RequestParameters requestParameters)

}