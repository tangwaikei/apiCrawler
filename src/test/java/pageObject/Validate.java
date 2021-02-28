package pageObject;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class Validate {
    public Map<String, List<String>> validate;

    public void validate() {
        for(Map.Entry<String, List<String>> kv: validate.entrySet()) {
            if(kv.getKey().equals("equalTo")) {
                assertThat(kv.getValue().get(0), equalTo(kv.getValue().get(1)));
            }
            if(kv.getKey().equals("greaterThan")) {
                assertThat(kv.getValue().get(0), greaterThan(kv.getValue().get(1)));
            }
        }
    }
}
