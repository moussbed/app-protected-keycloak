import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { KeycloakInstance } from 'keycloak-js';
declare var Keycloak: any;

@Injectable({
  providedIn: 'root',
})
export class KeycloakSecurityService {
  public kc: KeycloakInstance;

  constructor() {}

  async init() {
    console.log('Security Initialisation....');
    console.log(environment.PORT_KEYCLOAK);
    const port = environment.PORT_KEYCLOAK;
    this.kc = new Keycloak({
      url: `http://localhost:${port}/auth`,
      realm: 'ecom-realm',
      clientId: 'products-client-side',
    });

    await this.kc.init(
      // {onLoad: "login-required"}
      { onLoad: 'check-sso' }
    );
    console.log(this.kc.token);
  }
}
