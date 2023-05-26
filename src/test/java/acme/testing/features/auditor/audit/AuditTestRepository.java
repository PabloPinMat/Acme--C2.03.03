
package acme.testing.features.auditor.audit;

import org.springframework.data.jpa.repository.Query;

import acme.entities.audit.Audit;
import acme.entities.audit.AuditingRecord;
import acme.framework.repositories.AbstractRepository;

public interface AuditTestRepository extends AbstractRepository {

	@Query("select a from Audit a where a.code = :auditCode")
	Audit findAuditByCode(String auditCode);
	
	@Query("select ar from AuditingRecord ar where ar.subject = :subject")
	AuditingRecord findAuditingRecordBySubject(String subject);

}