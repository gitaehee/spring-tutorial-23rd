package com.ceos23.spring_boot;

import com.ceos23.spring_boot.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {}