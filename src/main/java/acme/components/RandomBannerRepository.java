
package acme.components;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banner.Banner;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface RandomBannerRepository extends AbstractRepository {

	@Query("select b from Banner b where b.displayPeriodBegin <= :date  and b.displayPeriodFinish > :date")
	Collection<Banner> findActiveBanners(Date date);

}
