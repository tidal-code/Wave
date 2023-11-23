package dev.tidalcode.wave.elementfinder;

import com.tidal.utils.filehandlers.Finder;
import dev.tidalcode.wave.exceptions.TimeoutException;
import dev.tidalcode.wave.verification.conditions.Condition;
import dev.tidalcode.wave.browser.Browser;
import dev.tidalcode.wave.webelement.ElementFinder;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

import static dev.tidalcode.wave.verification.conditions.collections.CollectionsCondition.*;
import static dev.tidalcode.wave.verification.criteria.Criteria.*;
import static dev.tidalcode.wave.webelement.ElementFinder.find;

public class ThenFindAndThenFindAllTests {

    @Before
    public void initialize() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        Browser.withOptions(options).open("file://" + Finder.findFilePath("components/elements/elements.html"));
    }

    @After
    public void terminate() {
        Browser.close();
    }

    @Test
    public void findThenFindThenFindTest() {
        String text = ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFind("tagName:p").getText();
        Assert.assertEquals("Tester", text);
    }

    @Test
    public void findThenFindThenFindMultipleTest() {
        String text = ElementFinder.find("div with id testid1").thenFind("div with id testid2").thenFind("div with id testid3").thenFind("tagName:p").getText();
        String text2 = ElementFinder.find("div with id testid1").thenFind("div with id testid2").thenFind("div with id testid3").thenFind("tagName:p").getText();
        Assert.assertEquals(text2, text);
    }

    @Test
    public void findThenFindThenFindTestWithShouldHave() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFind("tagName:p").shouldHave(Condition.exactText("Tester"));
    }

    @Test
    public void findThenFindThenFindTestWithShouldBeVisible() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").shouldBe(visible);
    }

    @Test(expected = TimeoutException.class)
    public void findThenFindThenFindTestWithShouldBeNotVisible() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").shouldBe(notVisible);
    }


    @Test
    public void findThenFindThenFindAttribute() {
        String text = ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").getAttribute("style");
        Assert.assertEquals("color: blue;", text);
    }

    @Test
    public void findThenFindThenFindTagName() {
        String tagName = ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").getTagName();
        Assert.assertEquals("div", tagName);
    }

    @Test
    public void findThenFindThenFindAll() {
        String text = ElementFinder.find("#testid1").thenFind("id:testid2").thenFindAll("tagName:div").get(1).getText();
        Assert.assertEquals("QA", text);
    }

    @Test
    public void findThenFindThenFindThenFindAll() {
        String text = ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).getText();
        Assert.assertEquals("Of", text);
    }

    @Test(expected = ComparisonFailure.class)
    public void findThenFindThenFindThenFindAllError() {
        String text = ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).getText();
        Assert.assertEquals("Automation", text);
    }

    @Test
    public void findThenFindAllShouldHave() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).shouldHave(Condition.exactText("Of"));
    }

    @Test
    public void findThenFindAllShouldHaveSize() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").shouldHave(size(3));
    }

    @Test
    public void findThenFindAllShouldHaveSizeGreater() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").shouldHave(sizeGreaterThan(2));
    }

    @Test
    public void findThenFindAllShouldHaveSizeLessThan() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").shouldHave(sizeLessThan(4));
    }

    @Test
    public void findThenFindAllShoulBeVisible() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).shouldBe(visible);
    }

    @Test(expected = TimeoutException.class)
    public void findThenFindAllShoulBeNotVisibleThrows() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).shouldBe(notVisible);
    }

    @Test
    public void findThenFindAllShoulBeEnabled() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).shouldBe(enabled);
    }

    @Test(expected = TimeoutException.class)
    public void findThenFindAllShoulBeEnabledThrows() {
        ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").get(1).shouldBe(notEnabled);
    }

    @Test
    public void findThenFindAllText() {
        List<String> values = new ArrayList<>();
        values.add("Tester");
        values.add("Of");
        values.add("Automation");

        List<String> text = ElementFinder.find("#testid1").thenFind("id:testid2").thenFind("id:testid3").thenFindAll("tagName:p").getAllText();
        Assert.assertEquals(values, text);
    }

}
