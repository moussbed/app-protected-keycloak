import { Component } from '@angular/core';
import { KeycloakSecurityService } from './services/keycloak-security.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend-client-side';
  constructor(public keycloakSecurityService:KeycloakSecurityService){

  }
  onLogout(): void{
      this.keycloakSecurityService.kc.logout();
  }
  onLogin(): void{
    this.keycloakSecurityService.kc.login();
  }
  onChangePassword(): void{
    this.keycloakSecurityService.kc.accountManagement()
  }
  hasAuthorize(role:string): boolean{
    return  this.keycloakSecurityService.kc.hasRealmRole(role);
  }
}
