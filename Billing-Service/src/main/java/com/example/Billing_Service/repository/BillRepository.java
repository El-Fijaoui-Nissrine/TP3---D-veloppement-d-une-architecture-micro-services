package com.example.Billing_Service.repository;

import com.example.Billing_Service.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository  extends JpaRepository<Bill, Long> {
}