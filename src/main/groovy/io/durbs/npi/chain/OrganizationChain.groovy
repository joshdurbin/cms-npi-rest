package io.durbs.npi.chain

import io.durbs.npi.domain.Organization
import io.durbs.npi.service.OrganizationService
import ratpack.groovy.handling.GroovyChainAction

import javax.inject.Inject

class OrganizationChain extends GroovyChainAction {

  private final OrganizationService organizationService

  @Inject
  OrganizationChain(OrganizationService organizationService) {

    this.organizationService = organizationService
  }

  @Override
  void execute() throws Exception {

    path(":npiCode") {
      final String npiCode = pathTokens['npiCode']

      get {
        organizationService.getOrganizationByCode(npiCode)
          .single()
          .subscribe { Organization organization ->
          if (organization) {
            render organization
          } else {
            clientError 404
          }
        }
      }
    }

    path(":postalCode") {
      final String postalCode = pathTokens['postalCode']

      get { ParametersChain.RequestParameters requestParameters ->
        organizationService.getOrganizationsByPostalCode(postalCode, requestParameters.pageNumber, requestParameters.pageSize)
          .subscribe { Organization organizations ->
          if (organizations) {
            render organizations
          } else {
            clientError 404
          }
        }
      }
    }

    get { ParametersChain.RequestParameters requestParameters ->
      organizationService.getOrganizations(requestParameters.pageNumber, requestParameters.pageSize)
        .subscribe { Organization organizations ->
        if (organizations) {
          render organizations
        } else {
          clientError 404
        }
      }
    }
  }
}
