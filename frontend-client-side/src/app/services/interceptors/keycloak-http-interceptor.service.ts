import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakSecurityService } from '../keycloak-security.service';

@Injectable({
  providedIn: 'root'
})
export class KeycloakHttpInterceptorService implements HttpInterceptor{

  constructor(private keycloakSecurityService:KeycloakSecurityService) { }

  intercept(request:HttpRequest<any>, next: HttpHandler){

    if(!this.keycloakSecurityService.kc.authenticated) return next.handle(request);
    let requestClone = request.clone({
      setHeaders: {
        Authorization: 'Bearer ' + this.keycloakSecurityService.kc.token
      }
    })
    return next.handle(requestClone)
  }
}
