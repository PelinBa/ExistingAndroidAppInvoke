package pb;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;

public class ExistingAppTest extends Configuration {

    @Test

    public void existingAppInvoke () throws InterruptedException {

        //Start any existing app (Photos) in the android device
        ((JavascriptExecutor) driver).executeScript("mobile: startActivity", ImmutableMap.of(
                "intent","com.google.android.apps.photos/com.google.android.apps.photos.home.HomeActivity"));

        //Click the second image
        driver.findElement(AppiumBy.xpath("(//android.widget.ImageView[@content-desc='Photo taken on Apr 7, 2025 6:33 PM'])[2]")).click();
        Thread.sleep(2000);
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        //Click the first image
        driver.findElement(AppiumBy.xpath("(//android.widget.ImageView[@content-desc='Photo taken on Apr 7, 2025 6:33 PM'])[1]")).click();

        //Swipe the images from the first image to the others
        int imageCount = 7;
        for (int i = 0; i < imageCount; i++) {
            Thread.sleep(1000);

            HashMap<String, Object> params = new HashMap<>();
            params.put("direction", "left");
            params.put("percent", 0.75);
            params.put("left", 100);
            params.put("top", 100);
            params.put("width", 200);
            params.put("height", 200);

            ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", params);

            if (i == 2) {
                driver.findElement(AppiumBy.accessibilityId("Favorite")).click();
                break;
            }
        }
        Thread.sleep(2000);

        driver.pressKey(new KeyEvent(AndroidKey.BACK));

        //Long press to the third image
        WebElement longPressImage = driver.findElement(AppiumBy.xpath("(//android.widget.ImageView[@content-desc='Photo taken on Apr 7, 2025 6:33 PM'])[3]"));
        ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) longPressImage).getId(), "duration", 1000
        ));

        //Click the share button
        driver.findElement(AppiumBy.accessibilityId("Share")).click();
        Thread.sleep(2000);
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        //Click the delete button
        driver.findElement(AppiumBy.accessibilityId("Move to trash")).click();

        //Get the message text, verify and print it
        String warningMessage = driver.findElement(AppiumBy.id("com.google.android.apps.photos:id/delete_everywhere_label")).getText();
        Assert.assertEquals(warningMessage, "Move to trash? It will be removed from all folders.");
        System.out.println("The message on the screen: " + warningMessage);
        //Click the trash box
        driver.findElement(AppiumBy.className("android.widget.ImageView")).click();

        //Click the menu button and go to the trash
        driver.findElement(AppiumBy.accessibilityId("Show Navigation Drawer")).click();
        Thread.sleep(1000);
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@resource-id='com.google.android.apps.photos:id/title' and @text='Trash']")).click();
        Thread.sleep(2000);

        //Long click the image and move to the photos
        WebElement longClickImage = driver.findElement(AppiumBy.accessibilityId("Photo taken on Apr 7, 2025 6:33 PM"));
        ((JavascriptExecutor) driver).executeScript("mobile: longClickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) longClickImage).getId(), "duration", 1000
        ));

        //Click the restore button
        driver.findElement(AppiumBy.id("com.google.android.apps.photos:id/photos_trash_ui_button_bar_restore_button")).click();

        //Click the "Restore"
        driver.findElement(AppiumBy.id("com.google.android.apps.photos:id/confirmation_button")).click();
        Thread.sleep(2000);

        String messageInTrash = driver.findElement(AppiumBy.id("com.google.android.apps.photos:id/photos_trash_ui_empty_state_title_view")).getText();
        Assert.assertEquals(messageInTrash, "Nothing in Trash");
        System.out.println("The message on the screen: " + messageInTrash);

        //Click the back arrow at the left top
        driver.findElement(AppiumBy.className("android.widget.ImageButton")).click();
    }
}

