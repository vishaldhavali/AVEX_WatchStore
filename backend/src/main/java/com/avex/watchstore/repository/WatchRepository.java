package com.avex.watchstore.repository;

import com.avex.watchstore.model.Watch;
import com.avex.watchstore.model.Watch.WatchCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Long> {

    List<Watch> findByCategory(WatchCategory category);

    List<Watch> findByFeatured(boolean featured);

    List<Watch> findByCategoryAndFeatured(WatchCategory category, boolean featured);
}
