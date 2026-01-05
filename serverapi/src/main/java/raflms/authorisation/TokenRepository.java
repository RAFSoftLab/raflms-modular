package raflms.authorisation;


import org.springframework.data.repository.ListCrudRepository;

public interface TokenRepository extends ListCrudRepository<Token, String> {
}
