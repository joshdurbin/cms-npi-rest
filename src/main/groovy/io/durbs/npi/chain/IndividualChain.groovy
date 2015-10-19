package io.durbs.npi.chain

import com.google.inject.Singleton
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Individual
import io.durbs.npi.service.IndividualService
import ratpack.groovy.handling.GroovyChainAction
import ratpack.jackson.Jackson

import javax.inject.Inject

@Singleton
class IndividualChain extends GroovyChainAction {

  @Inject
  private IndividualService individualService

  @Inject
  IndividualChain(IndividualService individualService) {

    this.individualService = individualService
  }

  @Override
  void execute() throws Exception {

    get('count') {

      individualService.count
        .single()
        .subscribe { Long count ->

        render Jackson.json(count)
      }
    }

    get('search') { RequestParameters requestParameters ->

      final String searchTerm = request.queryParams.q

      individualService.findByName(searchTerm, requestParameters)
        .toList()
        .subscribe { List<Individual> individuals ->

        render Jackson.json(individuals)
      }
    }

    get('in/:postalCode') { RequestParameters requestParameters ->

      final String postalCode = pathTokens.postalCode

      individualService.getAllForPracticePostalCode(postalCode, requestParameters)
        .toList()
        .subscribe { List<Individual> individuals ->

        if (individuals) {
          render Jackson.json(individuals)
        }
      }
    }

    get(':npiCode') {

      final String npiCode = pathTokens.npiCode

      individualService.getByNPICode(npiCode)
        .single()
        .subscribe { Individual individual ->

        if (individual) {
          render individual
        } else {
          clientError 404
        }
      }
    }

    get { RequestParameters requestParameters ->
      individualService.getAll(requestParameters)
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
