package io.durbs.npi.chain

import io.durbs.npi.domain.Individual
import io.durbs.npi.service.IndividualService
import ratpack.groovy.handling.GroovyChainAction
import ratpack.jackson.Jackson

import javax.inject.Inject

class IndividualChain extends GroovyChainAction {

  private final IndividualService individualService

  @Inject
  IndividualChain(IndividualService individualService) {

    this.individualService = individualService
  }

  @Override
  void execute() throws Exception {

    path(":npiCode") {
      final String npiCode = pathTokens['npiCode']

      byMethod {

        get {
          individualService.getIndividualByCode(npiCode)
            .single()
            .subscribe { Individual individual ->

            if (individual) {
              render individual
            } else {
              clientError 404
            }
          }
        }
      }
    }

    get { ParametersChain.RequestParameters requestParameters ->
      individualService.getIndividuals(requestParameters.pageNumber, requestParameters.pageSize)
        .toList()
        .subscribe { List<Individual> individual ->

        if (individual) {
          render Jackson.json(individual)
        } else {
          clientError 404
        }
      }
    }
  }
}
