import { Component, OnInit } from '@angular/core';
import { SupplierService } from './supplier.service';

interface Supplier {
  id:string|number;
  name:string;
  email:string;

}

@Component({
  selector: 'app-suppliers',
  templateUrl: './suppliers.component.html',
  styleUrls: ['./suppliers.component.css']
})
export class SuppliersComponent implements OnInit {

  suppliers:Array<Supplier>=[];

  constructor( private supplierService:SupplierService) { }

  ngOnInit(): void {
    this.supplierService.getSuppliers().subscribe(data=>{
      this.suppliers=data['_embedded']['suppliers'];
    })
  }

}
