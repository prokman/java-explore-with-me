package ewm.event.dto;

import ewm.event.model.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventPublicSpecification {
    public static Specification<Event> withFilter(GetPublicEventsParam param) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (param.getText() != null && !param.getText().isBlank()) {
                String searchString = "%" + param.getText().toLowerCase() + "%";
                Predicate annotationLike = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("annotation")), searchString);
                Predicate descriptionLike = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("description")), searchString);

                predicates.add(criteriaBuilder.or(annotationLike, descriptionLike));
            }

            if (param.getCategories() != null && !param.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(param.getCategories()));
            }

            if (param.getPaid() != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), param.getPaid()));
            }

            if (param.getOnlyAvailable() != null && Boolean.TRUE.equals(param.getOnlyAvailable())) {
                predicates.add(criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
            }

            if (param.getRangeStart() != null && param.getRangeEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("eventDate"), param.getRangeStart(), param.getRangeEnd()));
            }

            if (param.getSort() != null && !param.getSort().isBlank()) {
                if ("EVENT_DATE".equalsIgnoreCase(param.getSort())) {
                    query.orderBy(criteriaBuilder.desc(root.get("eventDate")));
                } else if ("VIEWS".equalsIgnoreCase(param.getSort())) {
                    query.orderBy(criteriaBuilder.desc(root.get("views")));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get("views")));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
