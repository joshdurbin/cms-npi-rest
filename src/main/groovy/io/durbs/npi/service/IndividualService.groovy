package io.durbs.npi.service

import groovy.transform.CompileStatic
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Individual
import rx.Observable

@CompileStatic
interface IndividualService {

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
  Observable<Individual> getAll(final RequestParameters requestParameters)

  /**
   *
   * @param postalCode
   * @param requestParameters
   * @return
   */
  Observable<Individual> getAllForPracticePostalCode(final String postalCode, final RequestParameters requestParameters)

  /**
   *
   * @param npiCode
   * @return
   */
  Observable<Individual> getByNPICode(final String npiCode)

  /**
   *
   * @param searchTerm
   * @param requestParameters
   * @return
   */
  Observable<Individual> findByName(final String searchTerm, final RequestParameters requestParameters)

}
