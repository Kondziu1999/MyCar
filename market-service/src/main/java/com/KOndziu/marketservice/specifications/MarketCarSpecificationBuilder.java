//package com.KOndziu.marketservice.specifications;
//
//import com.KOndziu.marketservice.modules.MarketCar;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class MarketCarSpecificationBuilder {
//
//    private final List<SearchCriteria> params;
//
//    public MarketCarSpecificationBuilder() {
//        params = new ArrayList<SearchCriteria>();
//    }
//
//    public MarketCarSpecificationBuilder with(String key, String operation, Object value) {
//        params.add(new SearchCriteria(key, operation, value));
//        return this;
//    }
//
//    public Specification<MarketCar> build() {
//        if (params.size() == 0) {
//            return null;
//        }
//
//        List<Specification> specs = params.stream()
//                .map(MarketCarSpecification::new)
//                .collect(Collectors.toList());
//
//        Specification<MarketCar> result = new MarketCarSpecification(params.get(0));
//
//        for (int i = 1; i < params.size(); i++) {
//            result = params.get(i)
//                    .isOrPredicate()
//                    ? Specification.where(result)
//                    .or(specs.get(i))
//                    : Specification.where(result)
//                    .and(specs.get(i));
//        }
//        return result;
//    }
//
//
//}
