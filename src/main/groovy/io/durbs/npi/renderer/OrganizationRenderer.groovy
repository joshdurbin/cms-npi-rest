package io.durbs.npi.renderer

import groovy.transform.CompileStatic
import io.durbs.npi.domain.Organization
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.render.GroovyRendererSupport
import ratpack.jackson.Jackson

@CompileStatic
class OrganizationRenderer extends GroovyRendererSupport<Organization> {

  @Override
  void render(GroovyContext context, Organization organization) throws Exception {
    context.byContent {
      json {
        context.render Jackson.json(organization)
      }
    }
  }
}