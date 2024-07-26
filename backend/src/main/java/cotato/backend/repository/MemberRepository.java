package cotato.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByLoginId(String loginId);

	Boolean existsByLoginId(String loginId);

	Member findByName(String name);

	List<Member> findByNameContaining(String name);
}