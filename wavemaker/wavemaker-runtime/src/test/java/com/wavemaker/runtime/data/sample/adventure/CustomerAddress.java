/*
 *  Copyright (C) 2009 WaveMaker Software, Inc.
 *
 *  This file is part of the WaveMaker Server Runtime.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wavemaker.runtime.data.sample.adventure;

// Generated Aug 18, 2007 5:20:14 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;

/**
 * CustomerAddress generated by hbm2java
 */
@SuppressWarnings("serial")
public class CustomerAddress implements java.io.Serializable {

    private CustomerAddressId id;

    private Customer customer;

    private Address address;

    private String addressType;

    private String rowguid;

    private Date modifiedDate;

    public CustomerAddress() {
    }

    public CustomerAddress(CustomerAddressId id, Customer customer, Address address, String addressType, String rowguid, Date modifiedDate) {
        this.id = id;
        this.customer = customer;
        this.address = address;
        this.addressType = addressType;
        this.rowguid = rowguid;
        this.modifiedDate = modifiedDate;
    }

    public CustomerAddressId getId() {
        return this.id;
    }

    public void setId(CustomerAddressId id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAddressType() {
        return this.addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getRowguid() {
        return this.rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}