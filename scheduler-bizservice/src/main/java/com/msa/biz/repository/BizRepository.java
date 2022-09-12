package com.msa.biz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msa.biz.entity.BizAppStore;

@Repository
public interface BizRepository extends JpaRepository <BizAppStore, Long> {

}
