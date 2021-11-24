
package tn.ittun.amzcmp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.ittun.amzcmp.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
}
