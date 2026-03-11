package utils;

import lombok.extern.log4j.Log4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log4j
public class CrawlHtml {

    /**
     * 通过 HttpClient抓取网页
     *
     * @param url
     * @return
     */
    public static String getHtml(String url) {
        //1.创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //2.创建GET请求方法实例
        HttpGet httpGet = new HttpGet(url);
        //3.调用Client实例执行GET实例，返回response
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            log.error(e);
        }

        //4.具体解析response以及结果处理
        //4.1获取状态码
        int status = response != null ? response.getStatusLine().getStatusCode() : 0;
        if (status >= 200 && status < 300) {
            //4.2获取实例
            HttpEntity entity = response.getEntity();
            //4.3获取html
            String html = null;
            try {
                html = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                log.error(e);
            }
            return html;

        } else {
            log.info("访问错误:" + status);
            return "访问错误：" + status;
        }
    }

    /**
     * 通过OkHttp进行抓取（同步）
     *
     * @param url
     * @return
     */
    public static String getHtmlByOkSync(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            return "访问错误：" + e.getMessage();
        }
    }

    public static String getHtmlBySelenium(String url) {
        // 0.设置 Chrome WebDriver 路径（根据实际情况设置）
        // System.setProperty("webdriver.chrome.driver",
        //         "D:\\000.Develop\\WebDriver1123\\chromedriver-win64\\chromedriver.exe");

        // 1.创建一个Chrome浏览器的选项配置的实例,定制Chrome浏览器的行为
        ChromeOptions chromeOptions = new ChromeOptions();

        // 1.1 关闭界面上的“Chrome正在受到自动软件的控制”提示
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        // 1.2 设置元素加载超时时间-隐式等待，这里超时后会报错并中断程序运行
        // chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(5)); // 设置等待元素超时时间
        // chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(5)); // 设置页面加载超时时间

        // 1.3设置旧无头模式 -- '=old'可以解决白屏问题
        chromeOptions.addArguments("--headless");

        // 2.使用上述配置初始化浏览器
        WebDriver driver = new ChromeDriver(chromeOptions);

        try {
            // 3.打开网页
            driver.get(url);

            // 4.执行其他操作
            // 4.1 找指定节点（这里是table[]节点），可以保证动态网页加载完全再爬取
            // By.ByXPath byXPath = new By.ByXPath("//table[@id='crewTable']");
            // WebElement element = driver.findElement(byXPath);

            // 5. 获取网页并返回
            return driver.getPageSource();
        } catch (WebDriverException e) {
            return "访问错误:" + e.getMessage();
        } finally {
            // 5.关闭浏览器，释放资源
            driver.quit();
        }
    }

}