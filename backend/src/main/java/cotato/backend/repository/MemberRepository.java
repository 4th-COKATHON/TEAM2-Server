package cotato.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByLoginId(String loginId);
}
