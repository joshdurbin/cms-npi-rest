package io.durbs.npi.chain

import com.google.common.base.Enums
import com.google.inject.Singleton
import groovy.transform.Immutable
import io.durbs.npi.config.RequestLimitsConfig
import ratpack.groovy.handling.GroovyChainAction

import javax.inject.Inject

@Singleton
class ParametersChain extends GroovyChainAction {

  private final RequestLimitsConfig requestLimitsConfig

  @Inject
  ParametersChain(RequestLimitsConfig requestLimitsConfig) {
    this.requestLimitsConfig = requestLimitsConfig
  }

  @Override
  void execute() throws Exception {

    all {

      Integer suppliedPageNumber = request.queryParams.pageNumber as Integer ?: requestLimitsConfig.defaultFirstPage

      Integer suppliedPageSize = request.queryParams.pageSize as Integer ?: requestLimitsConfig.defaultResultsPageSize
      Integer modifiedPageSize = suppliedPageSize > requestLimitsConfig.maxResultsPageSize ? requestLimitsConfig.maxResultsPageSize : suppliedPageSize

      context.next(single(new RequestParameters(pageNumber: suppliedPageNumber,
        pageSize: modifiedPageSize,
        status: Enums.getIfPresent(Status, request.queryParams.status ?: '').or(Status.active),
        orderBy: Enums.getIfPresent(OrderBy, request.queryParams.orderBy ?: '').or(OrderBy.lastUpdate),
        order: Enums.getIfPresent(Order, request.queryParams.order ?: '').or(Order.ascending))))
    }
  }

  @Immutable
  static class RequestParameters {

    Integer pageNumber
    Integer pageSize
    Status status
    OrderBy orderBy
    Order order

    Integer getOffSet() {
      pageNumber * pageSize
    }

    String getOrderCriteria() {

      if (order == Order.ascending) {
        "${orderBy}"
      } else {
        "-${orderBy}"
      }
    }
  }

  static enum Status {

    active, inactive, reactivated
  }

  static enum OrderBy {

    npiCode, lastUpdate
  }

  static enum Order {

    ascending, descending
  }
}
