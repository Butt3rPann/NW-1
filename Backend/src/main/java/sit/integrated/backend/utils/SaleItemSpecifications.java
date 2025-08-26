package sit.integrated.backend.utils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.entities.SaleItem;

import java.util.List;
import java.util.Objects;

public class SaleItemSpecifications {
    public static Specification<SaleItem> hasBrand(List<String> brands) {
        return (root, query, criteriaBuilder) -> {
            if (brands == null || brands.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<SaleItem, Brand> brandJoin = root.join("brand", JoinType.INNER);
            return brandJoin.get("name").in(brands);
        };
    }

    public static Specification<SaleItem> hasPriceGreaterThanOrEqual(Integer lower) {
        return (root, query, criteriaBuilder) -> {
            if (lower == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), lower);
        };
    }

    public static Specification<SaleItem> hasPriceLessThanOrEqual(Integer upper) {
        return (root, query, criteriaBuilder) -> {
            if (upper == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), upper);
        };
    }

    public static Specification<SaleItem> hasStorages(List<Integer> storages, boolean hasNull) {
        return (root, query, criteriaBuilder) -> {
            if (storages == null || storages.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Predicate predicates = criteriaBuilder.conjunction();
            List<Integer> nonNull = storages.stream().filter(Objects::nonNull).toList();
            if (!nonNull.isEmpty()) {
                predicates = root.get("storageGb").in(storages);
            }
            if (hasNull) {
                predicates = criteriaBuilder.or(predicates, criteriaBuilder.isNull(root.get("storageGb")));
            }
            return predicates;
        };
    }

    public static Specification<SaleItem> hasKeyWord(List<String> keywords) {
        return (root, query, cb) -> {
            if (keywords == null || keywords.isEmpty()) {
                return cb.conjunction();
            }

            Predicate predicate = cb.conjunction();
            for (String keyword : keywords) {
                if (keyword == null || keyword.isBlank()) continue;

                Predicate wordMatch =
                        cb.or(cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"),
                                cb.like(cb.lower(root.get("model")), "%" + keyword.toLowerCase() + "%"),
                                cb.like(cb.lower(root.get("color")), "%" + keyword.toLowerCase() + "%"));
                predicate = cb.and(predicate, wordMatch);
            }
            return predicate;
        };
    }
}
