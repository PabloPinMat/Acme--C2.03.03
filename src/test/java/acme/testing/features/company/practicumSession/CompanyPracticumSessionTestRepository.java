
package acme.testing.features.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.practicum.Practicum;
import acme.entities.session.PracticumSession;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumSessionTestRepository extends AbstractRepository {

	@Query("select p from Practicum p where p.company.userAccount.username = :username")
	Collection<Practicum> findManyPracticumsByCompanyUsername(String username);

	@Query("select ps from PracticumSession ps where ps.practicum.company.userAccount.username = :username")
	Collection<PracticumSession> findManyPracticumSessionsByCompanyUsername(String username);
}
