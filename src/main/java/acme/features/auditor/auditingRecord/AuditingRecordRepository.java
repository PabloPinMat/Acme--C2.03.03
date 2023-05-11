
package acme.features.auditor.auditingRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecord;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditingRecordRepository extends AbstractRepository {

	@Query("select a from Audit a where a.id = ?1")
	Audit findAuditById(int id);

	@Query("select A from Audit A where A.published = false")
	List<Audit> findAllPublishedAudits();

	@Query("select ar from AuditingRecord ar where ar.id=?1")
	AuditingRecord findAuditingRecordById(int id);

	@Query("select ar from AuditingRecord ar where ar.audit.id=?1")
	List<AuditingRecord> findAuditingRecordsByAuditId(int id);

}
