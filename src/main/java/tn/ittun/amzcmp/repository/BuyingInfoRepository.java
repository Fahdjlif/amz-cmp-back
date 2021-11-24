
package tn.ittun.amzcmp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.ittun.amzcmp.entity.BuyingInfo;


@Repository
public interface BuyingInfoRepository extends JpaRepository<BuyingInfo, Long>
{

}
