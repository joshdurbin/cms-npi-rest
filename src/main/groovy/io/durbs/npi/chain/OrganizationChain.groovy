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

    path(":npiCode") {
      final String npiCode = pathTokens['npiCode']

      byMethod {

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
    }

    get { ParametersChain.RequestParameters requestParameters ->
      organizationService.getOrganizations(requestParameters.pageNumber, requestParameters.pageSize)
        .toList()
        .subscribe { List<Organization> organizations ->
        if (organizations) {
          render Jackson.json(organizations)
        } else {
          clientError 404
        }
      }
    }
  }
}
