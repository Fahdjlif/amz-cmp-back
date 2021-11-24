
package tn.ittun.amzcmp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.ittun.amzcmp.entity.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long>
{
	@Query(value = "SELECT * FROM account_single_table WHERE locale_code = ?1", nativeQuery = true)
	Account loadAccountByLocaleCode(String localeCode);

	// TODO create test case
	@Query(value = "SELECT * FROM account_single_table WHERE locale_code != ?1 AND is_active = ?2", nativeQuery = true)
	List<Account> loadSecondaryAccounts(String localeCode, boolean isActive);
}
