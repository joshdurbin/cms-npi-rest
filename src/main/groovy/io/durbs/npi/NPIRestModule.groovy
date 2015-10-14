package io.durbs.npi

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import groovy.transform.CompileStatic
import io.durbs.npi.chain.IndividualChain
import io.durbs.npi.chain.OrganizationChain
import io.durbs.npi.chain.ParametersChain
import io.durbs.npi.renderer.IndividualRenderer
import io.durbs.npi.renderer.OrganizationRenderer
import io.durbs.npi.service.IndividualService
import io.durbs.npi.service.OrganizationService

@CompileStatic
class NPIRestModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(IndividualRenderer).in(Scopes.SINGLETON)
    bind(OrganizationRenderer).in(Scopes.SINGLETON)
    bind(IndividualService).in(Scopes.SINGLETON)
    bind(OrganizationService).in(Scopes.SINGLETON)
    bind(ParametersChain).in(Scopes.SINGLETON)
    bind(IndividualChain).in(Scopes.SINGLETON)
    bind(OrganizationChain).in(Scopes.SINGLETON)
  }
}
