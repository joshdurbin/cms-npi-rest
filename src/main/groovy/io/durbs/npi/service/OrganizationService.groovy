package io.durbs.npi.service

import com.google.inject.Singleton
import io.durbs.npi.domain.Organization
import org.bson.types.ObjectId
import org.mongodb.morphia.dao.BasicDAO
import ratpack.exec.Blocking
import rx.Observable

import javax.inject.Inject

@Singleton
class OrganizationService {

  @Inject
  private BasicDAO<Organization, ObjectId> organizationDao

  Observable<Organization> getOrganizations(final Integer page, final Integer pageSize) {

    Blocking.get {
      organizationDao.createQuery().limit(pageSize).offset(pageSize * page).iterator()
    }.observeEach()
  }

  Observable<Organization> getOrganizationByCode(final String npiCode) {

    Blocking.get {
      organizationDao.createQuery().field('npiCode').equal(npiCode).get()
    }.observe()
  }
}
