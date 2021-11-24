
package tn.ittun.amzcmp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.ittun.amzcmp.entity.SavedSearch;


@Repository
public interface SavedSearchRepository extends JpaRepository<SavedSearch, Long>
{

}
