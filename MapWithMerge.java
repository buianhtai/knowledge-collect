public static void main(String[] agrs) {

        final Map<Integer, Map<String, Integer>> orderByOrderIdAndAccountMap = new HashMap<>();

        List<Order> orders =
                Arrays.asList(
                        Order.builder().orderId(1).accountId("01").total(10).build(),
                        Order.builder().orderId(1).accountId("02").total(5).build(),
                        Order.builder().orderId(2).accountId("02").total(10).build(),
                        Order.builder().orderId(1).accountId("04").total(1).build(),
                        Order.builder().orderId(1).accountId("02").total(7).build()
                );
        orders.forEach(order -> {
            int totalCount = order.total;
            HashMap<String, Integer> schoolSaleMap = new java.util.HashMap<>();
            schoolSaleMap.put(order.accountId, totalCount);
            orderByOrderIdAndAccountMap.merge(order.orderId, schoolSaleMap,
                    (old, current) -> {
                        old.merge(order.getAccountId(), totalCount, Integer::sum);
                        return old;
                    });
        });
        log.debug("Map with merge {}", orderByOrderIdAndAccountMap);
    }

    @Builder
    @Data
    public static class Order {
        Integer orderId;
        String accountId;
        Integer total;
    }

default V merge(K key, V value, BiFunction<V, V, V> remappingFunction) {
    V oldValue = get(key);
    V newValue = (oldValue == null) ? value :
               remappingFunction.apply(oldValue, value);
    if (newValue == null) {
        remove(key);
    } else {
        put(key, newValue);
    }
    return newValue;
}
    
    ///Map with merge {1={01=10, 02=12, 04=1}, 2={02=10}}
