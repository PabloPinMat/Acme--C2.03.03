package acme.features.authenticated.audit;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedAuditRepository extends AbstractRepository {
	
	@Query("select a from Audit a")
	List<Audit> findAllAudits();
	
	@Query("select a from Audit a where a.course.id = ?1")
	Collection<Audit> findAuditsByCourseId(int id);
	
	@Query("select a from Audit a where a.id = ?1")
	Audit findAuditById(Integer id);

}
