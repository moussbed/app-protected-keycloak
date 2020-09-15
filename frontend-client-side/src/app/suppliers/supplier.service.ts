import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SupplierService {

  constructor(private http:HttpClient) { }

  /**
   * getSuppliers
   */
  public getSuppliers() {
   return  this.http.get("http://localhost:8083/suppliers")
  }
}
