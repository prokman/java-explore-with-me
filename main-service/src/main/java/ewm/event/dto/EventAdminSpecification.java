package ewm.event.dto;

import ewm.event.model.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventAdminSpecification {
    public static Specification<Event> withFilter(GetAdminEventsParam param) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (param.getUsers() != null && !param.getUsers().isEmpty()) {
                predicates.add(root.get("initiator").get("id").in(param.getUsers()));
            }

            if (param.getStates() != null && !param.getStates().isEmpty()) {
                predicates.add(root.get("eventState").in(param.getStates()));
            }

            if (param.getCategories() != null && !param.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(param.getCategories()));
            }

            if (param.getRangeStart() != null && param.getRangeEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("eventDate"), param.getRangeStart(), param.getRangeEnd()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
