import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products.component';
import { SuppliersComponent } from './suppliers/suppliers.component';
import { KeycloakSecurityService } from './services/keycloak-security.service';
import { KeycloakHttpInterceptorService } from './services/interceptors/keycloak-http-interceptor.service';

function keycloakFactory(keycloakSecurityService: KeycloakSecurityService){
    return () => keycloakSecurityService.init();
}

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    SuppliersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [ 
    {provide:APP_INITIALIZER, deps:[KeycloakSecurityService], useFactory:keycloakFactory, multi:true},
    {provide:HTTP_INTERCEPTORS, useClass:KeycloakHttpInterceptorService, multi:true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
