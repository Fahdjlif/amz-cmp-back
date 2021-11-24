
package tn.ittun.amzcmp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.ittun.amzcmp.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	@Query(value = "SELECT * FROM user_single_table WHERE email = ?1", nativeQuery = true)
	User loadUserByEmail(String email);

	@Query(value = "SELECT * FROM user_single_table WHERE token = ?1", nativeQuery = true)
	User loadUserByToken(String token);

}
