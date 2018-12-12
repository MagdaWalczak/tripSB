package tripSB;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tripSB.model.Customer;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRespository extends PagingAndSortingRepository<Customer, Long> {

    List<Customer> findByFirstName(@Param("firstName") String firstName);
}
