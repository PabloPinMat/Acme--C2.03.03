
package acme.features.auditor.audit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecord;
import acme.entities.audit.Mark;
import acme.entities.course.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Auditor;

@Repository
public interface AuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.userAccount.id = :id")
	Collection<Audit> findAuditsByAuditorId(int id);

	@Query("select a from Audit a where a.id = :id")
	Audit findAuditById(int id);

	@Query("select a from Audit a where a.code = :code")
	Audit findAuditByCode(String code);

	@Query("select ar from AuditingRecord ar where ar.audit = :audit")
	Collection<AuditingRecord> findAuditingRecordsByAudit(Audit audit);

	@Query("select ar.mark from AuditingRecord ar where ar.audit.id = :id")
	Collection<Mark> findMarksByAuditId(int id);

	@Query("select a from Auditor a where a.id = :id")
	Auditor findOneAuditorById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select c from Course c")
	Collection<Course> findAllCourses();
	
	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findPublishedCourses();

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findPublishedCourses();

}
