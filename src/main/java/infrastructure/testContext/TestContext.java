package infrastructure.testContext;

import com.microsoft.playwright.Page;
import io.cucumber.java.Scenario;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TestContext {
    private Scenario scenario;
    private Page newTabPage;
    private String selectedProductName;
}
