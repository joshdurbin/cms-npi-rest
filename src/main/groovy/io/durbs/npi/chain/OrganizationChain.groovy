package io.durbs.npi.chain

import com.google.inject.Singleton
import io.durbs.npi.domain.Organization
import io.durbs.npi.service.OrganizationService
import ratpack.groovy.handling.GroovyChainAction
import ratpack.jackson.Jackson

import javax.inject.Inject

@Singleton
class OrganizationChain extends GroovyChainAction {

  @Inject
  private OrganizationService organizationService

  @Override
  void execute() throws Exception {

    get('count') {

      organizationService.count
        .single()
        .subscribe { Long count ->

        render Jackson.json(count)
      }
    }


    get('search') { ParametersChain.RequestParameters requestParameters ->

      final String searchTerm = request.queryParams.q

      organizationService.findByName(searchTerm, requestParameters.pageNumber, requestParameters.pageSize)
        .toList()
        .subscribe { List<Organization> organizations ->

        render Jackson.json(organizations)
      }
    }

    get('in/:postalCode') { ParametersChain.RequestParameters requestParameters ->

      final String postalCode = pathTokens.postalCode

      organizationService.getAllForPracticePostalCode(postalCode, requestParameters.pageNumber, requestParameters.pageSize)
        .toList()
        .subscribe { List<Organization> organizations ->

        if (organizations) {
          render Jackson.json(organizations)
        }
      }
    }

    get(":npiCode") {
      final String npiCode = pathTokens.npiCode

      organizationService.getByNPICode(npiCode)
        .single()
        .subscribe { Organization organization ->
        if (organization) {
          render organization
        } else {
          clientError 404
        }
      }
    }

    get { ParametersChain.RequestParameters requestParameters ->
      organizationService.getAll(requestParameters.pageNumber, requestParameters.pageSize)
        .toList()
        .subscribe { List<Organization> organizations ->
        if (organizations) {
          render Jackson.json(organizations)
        }
      }
    }
  }
}
