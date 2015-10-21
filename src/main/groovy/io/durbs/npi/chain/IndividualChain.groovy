package io.durbs.npi.chain

import com.google.inject.Singleton
import io.durbs.npi.chain.ParametersChain.RequestParameters
import io.durbs.npi.domain.Individual
import io.durbs.npi.service.IndividualService
import ratpack.groovy.handling.GroovyChainAction
import ratpack.jackson.Jackson

@Singleton
class IndividualChain extends GroovyChainAction {

  @Override
  void execute() throws Exception {

    get('count') { IndividualService individualService ->

      individualService.count
        .single()
        .subscribe { Long count ->

        render Jackson.json(count)
      }
    }

    get('search') { IndividualService individualService ->

      final String searchTerm = request.queryParams.q

      individualService.findByName(searchTerm)
        .toList()
        .subscribe { List<Individual> individuals ->

        render Jackson.json(individuals)
      }
    }

    get('in/:postalCode') { IndividualService individualService, RequestParameters requestParameters ->

      final String postalCode = pathTokens.postalCode

      individualService.getAllForPracticePostalCode(postalCode, requestParameters)
        .toList()
        .subscribe { List<Individual> individuals ->

        if (individuals) {
          render Jackson.json(individuals)
        }
      }
    }

    get(':npiCode') { IndividualService individualService ->

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

    get { RequestParameters requestParameters, IndividualService individualService ->
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
