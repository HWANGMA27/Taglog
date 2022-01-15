package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.taglog.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    public List<Tag> findTagById(Long id);

    public Optional<Tag> findTagByName(String name);
}
