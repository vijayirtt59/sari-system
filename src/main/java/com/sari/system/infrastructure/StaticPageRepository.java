package com.sari.system.infrastructure;

import com.sari.system.domain.StaticPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticPageRepository extends JpaRepository<StaticPage, String> {
}
