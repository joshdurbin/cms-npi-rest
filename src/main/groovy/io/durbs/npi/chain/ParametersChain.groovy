package io.durbs.npi.chain

import groovy.transform.Immutable
import io.durbs.npi.config.RequestLimitsConfig
import ratpack.groovy.handling.GroovyChainAction

import javax.inject.Inject

class ParametersChain extends GroovyChainAction {

  private final RequestLimitsConfig requestLimitsConfig

  @Inject
  ParametersChain(RequestLimitsConfig requestLimitsConfig) {
    this.requestLimitsConfig = requestLimitsConfig
  }

  @Override
  void execute() throws Exception {

    all {

      Integer suppliedPageSize = request.queryParams.pageSize as Integer ?: requestLimitsConfig.defaultResultsPageSize
      Integer suppliedPageNumber = request.queryParams.page as Integer ?: requestLimitsConfig.defaultFirstPage

      Integer modifiedPageSize = suppliedPageSize > requestLimitsConfig.maxResultsPageSize ? requestLimitsConfig.maxResultsPageSize : suppliedPageSize

      context.next(single(new RequestParameters(pageNumber: suppliedPageNumber, pageSize: modifiedPageSize)))
    }
  }

  @Immutable
  static class RequestParameters {

    Integer pageNumber
    Integer pageSize
  }
}
