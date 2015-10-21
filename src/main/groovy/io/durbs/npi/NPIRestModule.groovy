package io.durbs.npi

import com.google.inject.AbstractModule
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.durbs.npi.chain.IndividualChain
import io.durbs.npi.chain.OrganizationChain
import io.durbs.npi.chain.ParametersChain
import io.durbs.npi.renderer.IndividualRenderer
import io.durbs.npi.renderer.OrganizationRenderer
import io.durbs.npi.service.IndividualService
import io.durbs.npi.service.OrganizationService

@CompileStatic
@Slf4j
class NPIRestModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(ParametersChain)

    bind(IndividualRenderer)
    bind(IndividualService)
    bind(IndividualChain)
    bind(OrganizationRenderer)
    bind(OrganizationService)
    bind(OrganizationChain)
  }

}
