import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapWitMerge {

  public static void main(String[] agrs) {

    final Map<Integer, Map<String, Integer>> saleSumaryByOderId = new HashMap<>();

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
      HashMap<String, Integer> saleSummaryByAccount = new java.util.HashMap<>();
      saleSummaryByAccount.put(order.accountId, totalCount);
      
      saleSumaryByOderId.merge(order.orderId, saleSummaryByAccount,
          (old, current) -> {
            old.merge(order.getAccountId(), totalCount, Integer::sum);
            return old;
          });
    });
    log.debug("Map with merge {}", saleSumaryByOderId);
          // Map with merge {1={01=10, 02=12, 04=1}, 2={02=10}}
  }

  @Builder
  @Data
  public static class Order {

    Integer orderId;
    String accountId;
    Integer total;
  }


}
