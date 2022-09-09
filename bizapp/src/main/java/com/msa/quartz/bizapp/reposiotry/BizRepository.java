package com.msa.quartz.bizapp.reposiotry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BizRepository extends JpaRepository ( BizAppStore, Long) {

}
