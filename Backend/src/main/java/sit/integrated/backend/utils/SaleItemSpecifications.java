package sit.integrated.backend.utils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.entities.SaleItem;

import java.util.List;

public class SaleItemSpecifications {
    public static Specification<SaleItem> inBrand(List<String> brands) {
        return (root, query, criteriaBuilder) -> {
            if (brands == null || brands.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<SaleItem, Brand> brandJoin = root.join("brands", JoinType.INNER);
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

//    public static Specification<SaleItem> inStorages(List<Integer> storages) {
//        return (root, query, criteriaBuilder) -> {
//            if (storages == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.in(root.get("storageGb"), storages);
//        };
//    }
}
