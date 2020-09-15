import { TestBed } from '@angular/core/testing';

import { KeycloakHttpInterceptorService } from './keycloak-http-interceptor.service';

describe('KeycloakHttpInterceptorService', () => {
  let service: KeycloakHttpInterceptorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeycloakHttpInterceptorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
